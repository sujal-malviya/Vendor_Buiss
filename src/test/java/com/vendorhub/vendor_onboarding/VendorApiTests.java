package com.vendorhub.vendor_onboarding;

import com.jayway.jsonpath.JsonPath;
import com.vendorhub.vendor_onboarding.entity.Role;
import com.vendorhub.vendor_onboarding.entity.Vendor;
import com.vendorhub.vendor_onboarding.repository.VendorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.UUID;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Calls the real API over HTTP (through MockMvc) with real JWT tokens, against an in-memory database.
 */
@SpringBootTest
class VendorApiTests {

    @Autowired
    WebApplicationContext context;

    @Autowired
    VendorRepository vendorRepository;

    MockMvc mvc;

    @BeforeEach
    void setUp()
    {
        mvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
    }

    @Test
    void registerRejectsShortPasswordAndBadEmail() throws Exception
    {
        mvc.perform(post("/api/auth/register").contentType(APPLICATION_JSON)
                        .content("{\"email\":\"not-an-email\",\"password\":\"x\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.password").exists())
                .andExpect(jsonPath("$.errors.email").exists());
    }

    @Test
    void vendorCannotSeeChangeOrDeleteAnotherVendorsBankDetails() throws Exception
    {
        String alice = register();
        createProfile(alice);
        long bankId = id(call(alice, post("/api/vendor/bank-details"),
                "{\"accountNumber\":\"50100011112221\",\"accountHolderName\":\"Alice\",\"ifscCode\":\"HDFC0001234\"}")
                .andExpect(status().isCreated()));

        String bob = register();
        call(bob, get("/api/vendor/bank-details"), null).andExpect(jsonPath("$").isEmpty());
        call(bob, get("/api/vendor/bank-details/" + bankId), null).andExpect(status().isNotFound());
        call(bob, patch("/api/vendor/bank-details/" + bankId), "{\"accountNumber\":\"999\"}").andExpect(status().isNotFound());
        call(bob, delete("/api/vendor/bank-details/" + bankId), null).andExpect(status().isNotFound());

        call(alice, get("/api/vendor/bank-details/" + bankId), null)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accountHolderName").value("Alice"));
    }

    @Test
    void bankDetailResponseMasksTheAccountNumber() throws Exception
    {
        String vendor = register();
        createProfile(vendor);
        call(vendor, post("/api/vendor/bank-details"),
                "{\"accountNumber\":\"50100011112221\",\"accountHolderName\":\"V\",\"ifscCode\":\"HDFC0001234\"}")
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.maskedAccountNumber").value("XXXXXXXXXX2221"))
                .andExpect(jsonPath("$.accountNumber").doesNotExist())
                .andExpect(content().string(not(containsString("50100011112221"))));
    }

    @Test
    void clientCannotChooseIdOrOwnerBecauseTheRequestDtoHasNoSuchFields() throws Exception
    {
        String alice = register();
        createProfile(alice);
        long aliceBankId = id(call(alice, post("/api/vendor/bank-details"),
                "{\"accountNumber\":\"50100011112221\",\"accountHolderName\":\"Alice\",\"ifscCode\":\"HDFC0001234\"}"));

        // Bob tries to overwrite Alice's row by sending her id and a profile in the body
        String bob = register();
        long bobProfileId = createProfile(bob);
        long bobBankId = id(call(bob, post("/api/vendor/bank-details"),
                "{\"id\":" + aliceBankId + ",\"vendorProfile\":{\"id\":1},\"accountNumber\":\"99999999\","
                        + "\"accountHolderName\":\"Bob\",\"ifscCode\":\"SBIN0000001\"}")
                .andExpect(status().isCreated()));

        org.junit.jupiter.api.Assertions.assertNotEquals(aliceBankId, bobBankId);
        call(alice, get("/api/vendor/bank-details/" + aliceBankId), null)
                .andExpect(jsonPath("$.accountHolderName").value("Alice"));
        call(bob, get("/api/vendor/profile/" + bobProfileId), null).andExpect(status().isOk());
    }

    @Test
    void patchChangesOnlyTheFieldsThatWereSent() throws Exception
    {
        String vendor = register();
        long profileId = createProfile(vendor);

        call(vendor, patch("/api/vendor/profile/" + profileId), "{\"name\":\"New Name\"}")
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("New Name"))
                .andExpect(jsonPath("$.address").value("MG Road, Bangalore"));
    }

    @Test
    void missingRecordReturns404WithMessageAndNoStackTrace() throws Exception
    {
        String vendor = register();
        call(vendor, get("/api/vendor/profile/999999"), null)
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.detail").value("Vendor profile not found: 999999"))
                .andExpect(jsonPath("$.trace").doesNotExist());
    }

    @Test
    void invalidBodyReturns400WithFieldErrors() throws Exception
    {
        String vendor = register();
        createProfile(vendor);
        call(vendor, post("/api/vendor/bank-details"), "{}")
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.accountNumber").value("account number is required"));
    }

    @Test
    void onlyAdminCanChangeSharedLists() throws Exception
    {
        String vendor = register();
        call(vendor, post("/api/dishes"), "{\"name\":\"Paneer Tikka\"}").andExpect(status().isForbidden());
        call(vendor, post("/api/vendor/event-types"), "{\"name\":\"Wedding\"}").andExpect(status().isForbidden());

        String admin = registerAdmin();
        long dishId = id(call(admin, post("/api/dishes"), "{\"name\":\"Paneer Tikka\"}").andExpect(status().isCreated()));

        call(vendor, get("/api/dishes/" + dishId), null).andExpect(status().isOk());
    }

    @Test
    void requiredItemCanBeDeleted() throws Exception
    {
        String admin = registerAdmin();
        long itemId = id(call(admin, post("/api/vendor/required-items"), "{\"name\":\"Tent\"}").andExpect(status().isCreated()));

        call(admin, delete("/api/vendor/required-items/" + itemId), null).andExpect(status().isNoContent());
        call(admin, get("/api/vendor/required-items/" + itemId), null).andExpect(status().isNotFound());
    }

    @Test
    void packageResponseDoesNotLeakTheVendorAccountOrPasswordHash() throws Exception
    {
        String vendor = register();
        call(vendor, post("/api/vendor/packages"), "{\"name\":\"Gold\",\"price\":500}")
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.vendor").doesNotExist())
                .andExpect(content().string(not(containsString("password"))));
    }

    @Test
    void deletingPackageAlsoRemovesItsDishes() throws Exception
    {
        String admin = registerAdmin();
        long dishId = id(call(admin, post("/api/dishes"), "{\"name\":\"Dal\"}"));

        String vendor = register();
        long packageId = id(call(vendor, post("/api/vendor/packages"), "{\"name\":\"Silver\"}"));
        long packageDishId = id(call(vendor, post("/api/vendor/package-dishes"),
                "{\"packageId\":" + packageId + ",\"dishId\":" + dishId + ",\"quantity\":2}")
                .andExpect(status().isCreated()));

        call(vendor, delete("/api/vendor/packages/" + packageId), null).andExpect(status().isNoContent());
        call(vendor, get("/api/vendor/package-dishes/" + packageDishId), null).andExpect(status().isNotFound());
    }

    @Test
    void vendorCannotAddDishesToAnotherVendorsPackage() throws Exception
    {
        String admin = registerAdmin();
        long dishId = id(call(admin, post("/api/dishes"), "{\"name\":\"Biryani\"}"));

        String alice = register();
        long alicePackage = id(call(alice, post("/api/vendor/packages"), "{\"name\":\"Alice Gold\"}"));

        String bob = register();
        call(bob, post("/api/vendor/package-dishes"),
                "{\"packageId\":" + alicePackage + ",\"dishId\":" + dishId + ",\"quantity\":1}")
                .andExpect(status().isNotFound());
    }

    @Test
    void deletingProfileAlsoDeletesItsSections() throws Exception
    {
        String vendor = register();
        long profileId = createProfile(vendor);
        long bankId = id(call(vendor, post("/api/vendor/bank-details"),
                "{\"accountNumber\":\"222\",\"accountHolderName\":\"V\",\"ifscCode\":\"SBIN0000001\"}"));

        call(vendor, delete("/api/vendor/profile/" + profileId), null).andExpect(status().isNoContent());
        call(vendor, get("/api/vendor/bank-details/" + bankId), null).andExpect(status().isNotFound());
    }

    @Test
    void secondProfileForSameVendorIsRejected() throws Exception
    {
        String vendor = register();
        createProfile(vendor);
        call(vendor, post("/api/vendor/profile"), "{\"name\":\"Again\",\"address\":\"Somewhere\"}")
                .andExpect(status().isConflict());
    }

    // ---------- helpers ----------

    private String register() throws Exception
    {
        return registerAs("vendor-" + UUID.randomUUID() + "@test.com");
    }

    private String registerAs(String email) throws Exception
    {
        String body = mvc.perform(post("/api/auth/register").contentType(APPLICATION_JSON)
                        .content("{\"email\":\"" + email + "\",\"password\":\"password123\"}"))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        return "Bearer " + JsonPath.read(body, "$.token");
    }

    private String registerAdmin() throws Exception
    {
        String email = "admin-" + UUID.randomUUID() + "@test.com";
        registerAs(email);
        Vendor admin = vendorRepository.findByEmail(email).orElseThrow();
        admin.setRole(Role.ADMIN);
        vendorRepository.save(admin);

        // Log in again so the new token carries the ADMIN scope
        String body = mvc.perform(post("/api/auth/login").contentType(APPLICATION_JSON)
                        .content("{\"identifier\":\"" + email + "\",\"password\":\"password123\"}"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        return "Bearer " + JsonPath.read(body, "$.token");
    }

    private long createProfile(String token) throws Exception
    {
        return id(call(token, post("/api/vendor/profile"), "{\"name\":\"Spice Caterers\",\"address\":\"MG Road, Bangalore\"}")
                .andExpect(status().isCreated()));
    }

    private ResultActions call(String token, org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder request,
                               String json) throws Exception
    {
        request.header("Authorization", token);
        if (json != null)
        {
            request.contentType(APPLICATION_JSON).content(json);
        }
        return mvc.perform(request);
    }

    private long id(ResultActions result) throws Exception
    {
        Number id = JsonPath.read(result.andReturn().getResponse().getContentAsString(), "$.id");
        return id.longValue();
    }
}
