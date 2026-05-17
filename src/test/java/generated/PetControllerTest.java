package generated;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

import javax.validation.Valid;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import generated.Owner;
import generated.OwnerRepository;
import generated.Pet;
import generated.PetController;
import generated.PetType;
import generated.PetTypeRepository;
import generated.PetValidator;
import generated.StringUtils;

public class PetControllerTest {

    private PetController petController;
    private OwnerRepository ownerRepository;
    private PetTypeRepository petTypeRepository;
    private Owner mockOwner;
    private Pet mockPet;

    @BeforeEach
    public void setUp() {
        ownerRepository = mock(OwnerRepository.class);
        petTypeRepository = mock(PetTypeRepository.class);
        petController = new PetController(ownerRepository, petTypeRepository);

        mockOwner = new Owner();
        mockOwner.setId(1);
        mockPet = new Pet();
        mockPet.setId(10);
    }

    @Test
    public void testProcessUpdateForm_ValidPet() {
        when(ownerRepository.findById(1)).thenReturn(Optional.of(mockOwner));
        when(pet.getBirthDate()).thenReturn(LocalDate.now().minusDays(1));

        String result = petController.processUpdateForm(mockOwner, mockPet, mock(BindingResult.class), mock(RedirectAttributes.class));

        assertEquals("redirect:/owners/{ownerId}", result);
    }

    @Test
    public void testProcessUpdateForm_DuplicatePetName() {
        when(ownerRepository.findById(1)).thenReturn(Optional.of(mockOwner));
        Pet existingPet = new Pet();
        existingPet.setId(20);
        existingPet.setName("Duplicate");
        mockPet.setName("Duplicate");

        BindingResult bindingResult = mock(BindingResult.class);
        petController.processUpdateForm(mockOwner, mockPet, bindingResult, mock(RedirectAttributes.class));

        verify(bindingResult).rejectValue("name", "duplicate", "already exists");
    }

    @Test
    public void testProcessUpdateForm_FutureBirthDate() {
        when(ownerRepository.findById(1)).thenReturn(Optional.of(mockOwner));
        LocalDate futureDate = LocalDate.now().plusDays(1);
        when(pet.getBirthDate()).thenReturn(futureDate);

        BindingResult bindingResult = mock(BindingResult.class);
        petController.processUpdateForm(mockOwner, mockPet, bindingResult, mock(RedirectAttributes.class));

        verify(bindingResult).rejectValue("birthDate", "typeMismatch.birthDate");
    }

    @Test
    public void testProcessUpdateForm_OwnerNotFound() {
        when(ownerRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> {
            petController.processUpdateForm(mockOwner, mockPet, mock(BindingResult.class), mock(RedirectAttributes.class));
        });
    }
}