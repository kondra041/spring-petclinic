package generated;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class PetControllerTest {

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(new PetController(mock(OwnerRepository.class), mock(PetTypeRepository.class))).build();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(new MockHttpServletRequest()));
    }

    @Test
    public void testFindOwnerWithExistingId() throws Exception {
        Owner owner = new Owner();
        when(mock(OwnerRepository.class).findById(1)).thenReturn(Optional.of(owner));

        mockMvc.perform(MockMvcRequestBuilders.get("/owners/1/pets/new"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("owner"))
                .andExpect(MockMvcResultMatchers.view().name("pets/createOrUpdatePetForm"));
    }

    @Test
    public void testFindOwnerWithNonExistingId() throws Exception {
        when(mock(OwnerRepository.class).findById(2)).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders.get("/owners/2/pets/new"))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }
}