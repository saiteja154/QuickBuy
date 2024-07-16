package com.shopme.admin.brand;

import com.shopme.common.entity.Brand;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.assertj.core.api.Assertions.assertThat;

public class BrandServiceTests {


        @MockBean
        private BrandRepository brandRepository;

        @InjectMocks
        private BrandService brandService;

        @Test
        public void testCheckUniqueInNewModeReturnDuplicate() {
            Integer id = null;
            String name = "Acer";
            Brand brand = new Brand(name);

            Mockito.when(brandRepository.findByName(name)).thenReturn(brand);

            String result = brandService.checkUnique(id, name);
            assertThat(result).isEqualTo("Duplicate");
        }

        @Test
        public void testCheckUniqueInNewModeReturnOK() {
            Integer id = null;
            String name = "AMD";

            Mockito.when(brandRepository.findByName(name)).thenReturn(null);

            String result = brandService.checkUnique(id, name);
            assertThat(result).isEqualTo("OK");
        }

        @Test
        public void testCheckUniqueInEditModeReturnDuplicate() {
            Integer id = 1;
            String name = "Canon";
            Brand brand = new Brand(id, name);

            Mockito.when(brandRepository.findByName(name)).thenReturn(brand);

            String result = brandService.checkUnique(2, "Canon");
            assertThat(result).isEqualTo("Duplicate");
        }

        @Test
        public void testCheckUniqueInEditModeReturnOK() {
            Integer id = 1;
            String name = "Acer";
            Brand brand = new Brand(id, name);

            Mockito.when(brandRepository.findByName(name)).thenReturn(brand);

            String result = brandService.checkUnique(id, "Acer Ltd");
            assertThat(result).isEqualTo("OK");
        }
}
