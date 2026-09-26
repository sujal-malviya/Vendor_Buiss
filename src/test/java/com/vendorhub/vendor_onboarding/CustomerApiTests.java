package com.vendorhub.vendor_onboarding;

import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.concurrent.atomic.AtomicLong;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class CustomerApiTests {

    // Unique email/phone per call, so tests don't clash with each other
    private static final AtomicLong COUNTER = new AtomicLong(System.nanoTime() % 1_000_000);

    @Autowired
    WebApplicationContext context;

    MockMvc mvc;

    @BeforeEach
    void setUp()
    {
        mvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
    }

    @Test
    void registerReturnsTokenAndMeShowsTheCustomerWithoutPassword() throws Exception
    {
        long n = COUNTER.incrementAndGet();
        String token = registerCustomer("asha" + n + "@test.com", phone(n));

        mvc.perform(get("/api/customer/me").header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Asha"))
                .andExpect(jsonPath("$.email").value("asha" + n + "@test.com"))
                .andExpect(jsonPath("$.password").doesNotExist())
                .andExpect(content().string(not(containsString("$2a$"))));
    }

    @Test
    void duplicateEmailOrPhoneReturns409() throws Exception
    {
        long n = COUNTER.incrementAndGet();
        registerCustomer("dup" + n + "@test.com", phone(n));

        mvc.perform(post("/api/customer/auth/register").contentType(APPLICATION_JSON)
                        .content(registerJson("dup" + n + "@test.com", phone(COUNTER.incrementAndGet()))))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.detail").value("Email is already registered"));

        mvc.perform(post("/api/customer/auth/register").contentType(APPLICATION_JSON)
                        .content(registerJson("other" + n + "@test.com", phone(n))))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.detail").value("Phone number is already registered"));
    }

    @Test
    void invalidRegistrationReturns400WithFieldErrors() throws Exception
    {
        mvc.perform(post("/api/customer/auth/register").contentType(APPLICATION_JSON)
                        .content("{\"email\":\"not-an-email\",\"phoneNumber\":\"12\",\"password\":\"short\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.name").exists())
                .andExpect(jsonPath("$.errors.email").exists())
                .andExpect(jsonPath("$.errors.phoneNumber").exists())
                .andExpect(jsonPath("$.errors.password").exists());
    }

    @Test
    void loginWorksWithEmailOrPhoneAndRejectsWrongPassword() throws Exception
    {
        long n = COUNTER.incrementAndGet();
        registerCustomer("login" + n + "@test.com", phone(n));

        login("login" + n + "@test.com", "password123").andExpect(status().isOk()).andExpect(jsonPath("$.token").exists());
        login(phone(n), "password123").andExpect(status().isOk());
        login("login" + n + "@test.com", "wrong-password").andExpect(status().isUnauthorized());
        login("nobody" + n + "@test.com", "password123").andExpect(status().isUnauthorized());
    }

    @Test
    void customerTokenCannotReachVendorData() throws Exception
    {
        long n = COUNTER.incrementAndGet();
        String customer = registerCustomer("c" + n + "@test.com", phone(n));

        mvc.perform(get("/api/vendor/profile").header("Authorization", customer)).andExpect(status().isForbidden());
        mvc.perform(get("/api/vendor/bank-details").header("Authorization", customer)).andExpect(status().isForbidden());
        mvc.perform(get("/api/vendor/me").header("Authorization", customer)).andExpect(status().isForbidden());

        // Shared lists are readable by everyone who is logged in
        mvc.perform(get("/api/dishes").header("Authorization", customer)).andExpect(status().isOk());
    }

    @Test
    void vendorTokenCannotReachCustomerEndpointsAndVendorLoginStillWorks() throws Exception
    {
        long n = COUNTER.incrementAndGet();
        String vendorEmail = "v" + n + "@test.com";
        String body = mvc.perform(post("/api/auth/register").contentType(APPLICATION_JSON)
                        .content("{\"email\":\"" + vendorEmail + "\",\"password\":\"password123\"}"))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        String vendor = "Bearer " + JsonPath.read(body, "$.token");

        mvc.perform(get("/api/customer/me").header("Authorization", vendor)).andExpect(status().isForbidden());

        // Vendor login still goes through Spring's AuthenticationManager and must keep working
        mvc.perform(post("/api/auth/login").contentType(APPLICATION_JSON)
                        .content("{\"identifier\":\"" + vendorEmail + "\",\"password\":\"password123\"}"))
                .andExpect(status().isOk());
    }

    @Test
    void customerCanSearchVendorsByCity() throws Exception
    {
        long n = COUNTER.incrementAndGet();
        String city = "Testcity" + n;

        // A vendor with a profile, business info and a service area in that city
        String body = mvc.perform(post("/api/auth/register").contentType(APPLICATION_JSON)
                        .content("{\"email\":\"sv" + n + "@test.com\",\"password\":\"password123\"}"))
                .andReturn().getResponse().getContentAsString();
        String vendor = "Bearer " + JsonPath.read(body, "$.token");
        mvc.perform(post("/api/vendor/profile").header("Authorization", vendor).contentType(APPLICATION_JSON)
                        .content("{\"name\":\"Search Caterers\",\"address\":\"Main Road\",\"businessInfo\":{\"name\":\"Search Caterers LLP\","
                                + "\"contactDetail\":\"9000000000\",\"address\":\"Main Road\",\"gstNumber\":\"27AAFCG3456D1Z4\","
                                + "\"fssaiNumber\":\"11223344556604\",\"yearsInBusiness\":4}}"))
                .andExpect(status().isCreated());
        mvc.perform(post("/api/vendor/service-area").header("Authorization", vendor).contentType(APPLICATION_JSON)
                        .content("{\"maxPeople\":500,\"servicePin\":\"411038\",\"serviceCity\":\"" + city + "\",\"serviceCountry\":\"India\","
                                + "\"orderSize\":300,\"minimumOrderSize\":200,\"maximumOrderSize\":500,\"minOrderValue\":350}"))
                .andExpect(status().isCreated());

        String customer = registerCustomer("search" + n + "@test.com", phone(n));

        // City match ignores upper/lower case
        mvc.perform(get("/api/customer/vendors?city=" + city.toLowerCase()).header("Authorization", customer))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements").value(1))
                .andExpect(jsonPath("$.content[0].businessName").value("Search Caterers LLP"))
                .andExpect(jsonPath("$.content[0].startingPrice").value(350))
                .andExpect(jsonPath("$.content[0].maxPeople").value(500));

        mvc.perform(get("/api/customer/vendors?city=NoSuchCity" + n).header("Authorization", customer))
                .andExpect(jsonPath("$.totalElements").value(0));

        // No city: every vendor with business info and a service area (at least this one)
        mvc.perform(get("/api/customer/vendors?size=100").header("Authorization", customer))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[?(@.city == '" + city + "')]").exists());

        // It is a customer endpoint
        mvc.perform(get("/api/customer/vendors").header("Authorization", vendor)).andExpect(status().isForbidden());
    }

    // ---------- helpers ----------

    private static String phone(long n)
    {
        return "9" + String.format("%09d", n % 1_000_000_000L);
    }

    private static String registerJson(String email, String phone)
    {
        return "{\"name\":\"Asha\",\"email\":\"" + email + "\",\"phoneNumber\":\"" + phone + "\",\"password\":\"password123\"}";
    }

    private String registerCustomer(String email, String phone) throws Exception
    {
        String body = mvc.perform(post("/api/customer/auth/register").contentType(APPLICATION_JSON)
                        .content(registerJson(email, phone)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        return "Bearer " + JsonPath.read(body, "$.token");
    }

    private org.springframework.test.web.servlet.ResultActions login(String identifier, String password) throws Exception
    {
        return mvc.perform(post("/api/customer/auth/login").contentType(APPLICATION_JSON)
                .content("{\"identifier\":\"" + identifier + "\",\"password\":\"" + password + "\"}"));
    }
}
