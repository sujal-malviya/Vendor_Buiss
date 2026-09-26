package com.vendorhub.vendor_onboarding.repository;

import com.vendorhub.vendor_onboarding.dto.VendorSearchResponse;
import com.vendorhub.vendor_onboarding.entity.VendorProfile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface VendorProfileRepository extends JpaRepository<VendorProfile,Long> {

    Optional<VendorProfile> findByVendorId(Long vendorId);

    Optional<VendorProfile> findByIdAndVendorId(Long id, Long vendorId);

    boolean existsByVendorId(Long vendorId);

    /*
     * Customer search. Only vendors who have filled in BOTH business info and a service area appear
     * (inner JOINs), because a customer can't book a vendor without them.
     *
     * Two queries instead of one "(:city IS NULL OR ...)" query: when city is null, PostgreSQL can't tell
     * the parameter's type and fails with "function lower(bytea) does not exist".
     *
     * The countQuery is written out because Spring can't reliably work out the total count
     * from a query that uses "SELECT new ...".
     */
    @Query(value = """
            SELECT new com.vendorhub.vendor_onboarding.dto.VendorSearchResponse(
                    vp.id, b.name, s.serviceCity, s.minOrderValue, s.maxPeople)
            FROM VendorProfile vp
            JOIN VendorBusinessInfo b ON b.vendorProfile = vp
            JOIN VendorServiceArea s ON s.vendorProfile = vp
            """,
            countQuery = """
            SELECT COUNT(vp)
            FROM VendorProfile vp
            JOIN VendorBusinessInfo b ON b.vendorProfile = vp
            JOIN VendorServiceArea s ON s.vendorProfile = vp
            """)
    Page<VendorSearchResponse> searchAllVendors(Pageable pageable);

    // Same as above, but only one city, ignoring upper/lower case
    @Query(value = """
            SELECT new com.vendorhub.vendor_onboarding.dto.VendorSearchResponse(
                    vp.id, b.name, s.serviceCity, s.minOrderValue, s.maxPeople)
            FROM VendorProfile vp
            JOIN VendorBusinessInfo b ON b.vendorProfile = vp
            JOIN VendorServiceArea s ON s.vendorProfile = vp
            WHERE LOWER(s.serviceCity) = LOWER(:city)
            """,
            countQuery = """
            SELECT COUNT(vp)
            FROM VendorProfile vp
            JOIN VendorBusinessInfo b ON b.vendorProfile = vp
            JOIN VendorServiceArea s ON s.vendorProfile = vp
            WHERE LOWER(s.serviceCity) = LOWER(:city)
            """)
    Page<VendorSearchResponse> searchVendorsByCity(@Param("city") String city, Pageable pageable);
}
