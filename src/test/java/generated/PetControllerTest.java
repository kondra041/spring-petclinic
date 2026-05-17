package generated;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Optional;
import javax.validation.Valid;

public class PetControllerTest {

    private OwnerRepository owners;
    private PetTypeRepository types;
    private PetController petController;

    @BeforeEach
    public void setUp() {
        owners = mock(OwnerRepository.class);
        types = mock(PetTypeRepository.class);
        petController = new PetController(owners, types);
    }

    @Test
    public void testProcessUpdateForm_ExistingPetNameConflict() {
        Owner owner = new Owner();
        Pet existingPet = new Pet();
        existingPet.setName("existingName");
        existingPet.setId(1L);

        when(owners.findById(anyInt())).thenReturn(Optional.of(owner));
        when(owner.getPet("newName", false)).thenReturn(existingPet);

        Pet pet = new Pet();
        pet.setName("existingName");

        String result = petController.processUpdateForm(owner, pet, mock(BindingResult.class), mock(RedirectAttributes.class));

        assertEquals(VIEWS_PETS_CREATE_OR_UPDATE_FORM, result);
    }

    @Test
    public void testProcessUpdateForm_InvalidBirthDate() {
        Owner owner = new Owner();

        Pet pet = new Pet();
        pet.setBirthDate(LocalDate.now().plusDays(1));

        String result = petController.processUpdateForm(owner, pet, mock(BindingResult.class), mock(RedirectAttributes.class));

        assertEquals(VIEWS_PETS_CREATE_OR_UPDATE_FORM, result);
    }

    @Test
    public void testProcessUpdateForm_Success() {
        Owner owner = new Owner();
        Pet existingPet = new Pet();
        existingPet.setId(1L);

        when(owners.findById(anyInt())).thenReturn(Optional.of(owner));
        when(owner.getPet("newName", false)).thenReturn(null);

        Pet pet = new Pet();
        pet.setName("newName");
        pet.setBirthDate(LocalDate.now());

        BindingResult result = mock(BindingResult.class);
        when(result.hasErrors()).thenReturn(false);

        String resultUrl = petController.processUpdateForm(owner, pet, result, mock(RedirectAttributes.class));

        assertEquals("redirect:/owners/{ownerId}", resultUrl);
    }
}