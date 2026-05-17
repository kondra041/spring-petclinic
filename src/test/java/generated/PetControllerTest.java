package generated;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

public class PetControllerTest {

    private PetController petController;
    private OwnerRepository owners;
    private PetTypeRepository types;

    @BeforeEach
    public void setUp() {
        owners = mock(OwnerRepository.class);
        types = mock(PetTypeRepository.class);
        petController = new PetController(owners, types);
    }

    @Test
    public void testProcessCreationForm_withDuplicatePetName() {
        Owner owner = new Owner();
        Pet existingPet = new Pet();
        existingPet.setName("Test");
        when(owners.findById(1)).thenReturn(Optional.of(owner));
        when(owner.getPet("Test", true)).thenReturn(existingPet);

        ModelMap modelMap = new ModelMap();
        Pet pet = new Pet();
        pet.setName("Test");

        String result = petController.processCreationForm(owner, pet, mock(BindingResult.class), mock(RedirectAttributes.class));

        assertEquals(VIEWS_PETS_CREATE_OR_UPDATE_FORM, result);
    }

    @Test
    public void testProcessCreationForm_withValidPet() {
        Owner owner = new Owner();
        when(owners.findById(1)).thenReturn(Optional.of(owner));
        when(owner.getPet("Test", true)).thenReturn(null);

        ModelMap modelMap = new ModelMap();
        Pet pet = new Pet();
        pet.setName("Test");
        pet.setBirthDate(LocalDate.now());

        String result = petController.processCreationForm(owner, pet, mock(BindingResult.class), mock(RedirectAttributes.class));

        assertEquals("redirect:/owners/{ownerId}", result);
    }

    @Test
    public void testProcessCreationForm_withInvalidBirthDate() {
        Owner owner = new Owner();
        when(owners.findById(1)).thenReturn(Optional.of(owner));
        when(owner.getPet("Test", true)).thenReturn(null);

        ModelMap modelMap = new ModelMap();
        Pet pet = new Pet();
        pet.setName("Test");
        pet.setBirthDate(LocalDate.now().plusDays(1));

        String result = petController.processCreationForm(owner, pet, mock(BindingResult.class), mock(RedirectAttributes.class));

        assertEquals(VIEWS_PETS_CREATE_OR_UPDATE_FORM, result);
    }
}