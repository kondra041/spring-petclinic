import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.mock.web.WebDataBinder;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

class PetControllerTest {

    @Mock
    private OwnerRepository owners;

    @Mock
    private PetTypeRepository types;

    @InjectMocks
    private PetController petController;

    private static final int OWNER_ID = 1;
    private static final Integer PET_ID = 1;
    private static final String PET_NAME = "Buddy";
    private static final LocalDate BIRTH_DATE = LocalDate.now();

    @Test
    void processUpdateForm_WhenDuplicatePetName_ShouldReject() {
        // Setup mock owner with existing pet
        Owner owner = new Owner();
        Pet existingPet1 = new Pet(PET_ID, PET_NAME, null, null);
        Pet existingPet2 = new Pet(PET_ID + 1, PET_NAME, null, null);
        owner.addPet(existingPet1);
        owner.addPet(existingPet2);

        when(owners.findById(anyInt())).thenReturn(Optional.of(owner));

        // Call method under test
        BindingResult result = new BindingResult();
        ModelMap model = new ModelMap();
        String viewName = petController.processUpdateForm(owner, existingPet2, result, mock(RedirectAttributes.class));

        assertEquals(VIEWS_PETS_CREATE_OR_UPDATE_FORM, viewName);
        verify(result).hasErrors();
    }

    @Test
    void processUpdateForm_WhenFutureBirthDate_ShouldReject() {
        // Setup owner with a pet
        Owner owner = new Owner();
        Pet pet = new Pet(PET_ID, "Old", null, null);
        owner.addPet(pet);

        when(owners.findById(anyInt())).thenReturn(Optional.of(owner));

        // Call method under test
        Pet updatedPet = new Pet(PET_ID, "New", BIRTH_DATE.plusDays(1), null);
        BindingResult result = mock(BindingResult.class);
        RedirectAttributes redirect = mock(RedirectAttributes.class);

        petController.processUpdateForm(owner, updatedPet, result, redirect);

        verify(result).rejectValue("birthDate", anyString(), anyString());
    }

    @Test
    void processUpdateForm_WhenValid_ShouldUpdate() {
        // Setup owner with existing pet
        Owner owner = new Owner();
        Pet existingPet = new Pet(PET_ID, "Old", null, null);
        owner.addPet(existingPet);

        when(owners.findById(anyInt())).thenReturn(Optional.of(owner));

        // Call method under test
        Pet updatedPet = new Pet(PET_ID, "Updated", BIRTH_DATE, null);
        BindingResult result = mock(BindingResult.class);
        RedirectAttributes redirect = mock(RedirectAttributes.class);

        String viewName = petController.processUpdateForm(owner, updatedPet, result, redirect);

        // Verify
        assertEquals("redirect:/owners/" + OWNER_ID, viewName);
        verify(redirect).addFlashAttribute(eq("message"), eq("Pet details has been edited"));
        verify(owners).save(owner);
    }
}