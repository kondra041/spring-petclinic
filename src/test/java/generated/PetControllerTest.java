package generated;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Optional;

import javax.validation.Valid;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

public class PetControllerTest {

    private PetController petController;
    private OwnerRepository ownerRepositoryMock;
    private PetTypeRepository petTypeRepositoryMock;
    private Owner ownerMock;
    private Pet petMock;

    @BeforeEach
    public void setUp() {
        ownerRepositoryMock = mock(OwnerRepository.class);
        petTypeRepositoryMock = mock(PetTypeRepository.class);
        petController = new PetController(ownerRepositoryMock, petTypeRepositoryMock);

        ownerMock = mock(Owner.class);
        when(ownerRepositoryMock.findById(1)).thenReturn(Optional.of(ownerMock));

        petMock = mock(Pet.class);
        when(petMock.getName()).thenReturn("TestPet");
        when(petMock.isNew()).thenReturn(true);
        when(petMock.getBirthDate()).thenReturn(LocalDate.now().minusDays(1));
    }

    @Test
    public void testProcessCreationForm_ValidPet() {
        RedirectAttributes redirectAttributes = mock(RedirectAttributes.class);
        BindingResult bindingResult = mock(BindingResult.class);

        String result = petController.processCreationForm(ownerMock, petMock, bindingResult, redirectAttributes);

        assertEquals("redirect:/owners/{ownerId}", result);
        verify(ownerMock).addPet(petMock);
        verify(ownersRepositoryMock).save(ownerMock);
        verify(redirectAttributes).addFlashAttribute("message", "New Pet has been Added");
    }

    @Test
    public void testProcessCreationForm_DuplicateName() {
        when(ownerMock.getPet("TestPet", true)).thenReturn(new Pet());

        RedirectAttributes redirectAttributes = mock(RedirectAttributes.class);
        BindingResult bindingResult = mock(BindingResult.class);

        String result = petController.processCreationForm(ownerMock, petMock, bindingResult, redirectAttributes);

        assertEquals(VIEWS_PETS_CREATE_OR_UPDATE_FORM, result);
        verify(bindingResult).rejectValue("name", "duplicate", "already exists");
    }

    @Test
    public void testProcessCreationForm_FutureBirthDate() {
        when(petMock.getBirthDate()).thenReturn(LocalDate.now().plusDays(1));

        RedirectAttributes redirectAttributes = mock(RedirectAttributes.class);
        BindingResult bindingResult = mock(BindingResult.class);

        String result = petController.processCreationForm(ownerMock, petMock, bindingResult, redirectAttributes);

        assertEquals(VIEWS_PETS_CREATE_OR_UPDATE_FORM, result);
        verify(bindingResult).rejectValue("birthDate", "typeMismatch.birthDate");
    }

    @Test
    public void testProcessCreationForm_InvalidOwner() {
        when(ownerRepositoryMock.findById(1)).thenReturn(Optional.empty());

        RedirectAttributes redirectAttributes = mock(RedirectAttributes.class);
        BindingResult bindingResult = mock(BindingResult.class);

        assertThrows(IllegalArgumentException.class, () -> {
            petController.processCreationForm(ownerMock, petMock, bindingResult, redirectAttributes);
        });
    }
}