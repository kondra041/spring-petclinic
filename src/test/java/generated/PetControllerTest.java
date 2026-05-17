import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.mock.web.flash.MockRedirectAttributes;
import org.springframework.validation.BindingResult;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class PetControllerTest {

    @Mock
    private OwnerRepository ownerRepository;

    @Mock
    private PetTypeRepository petTypeRepository;

    @InjectMocks
    private PetController petController;

    private static final int ownerId = 1;
    private static final Integer petId = 2;
    private static final String message = "New Pet has been Added";
    private static final LocalDate today = LocalDate.now();

    @Test
    void testValidPetCreation() {
        // Arrange
        Owner owner = new Owner();
        Pet pet = new Pet();
        owner.addPet(pet);
        
        when(ownerRepository.findById(ownerId)).thenReturn(Optional.of(owner));
        when(owner.getPet(pet.getName(), true)).thenReturn(null);

        ModelMap model = new ModelMap();
        RedirectAttributes redirectAttributes = new MockRedirectAttributes();

        // Act
        String result = petController.processCreationForm(owner, pet, new BindingResult(), redirectAttributes);

        // Assert
        assertEquals("redirect:/owners/1", result);
        verify(ownerRepository).save(owner);
    }

    @Test
    void testInvalidPetBirthDate() {
        // Arrange
        Pet pet = new Pet();
        pet.setBirthDate(today.plusDays(1));
        
        Owner owner = new Owner();
        owner.addPet(pet);

        when(ownerRepository.findById(ownerId)).thenReturn(Optional.of(owner));

        ModelMap model = new ModelMap();
        RedirectAttributes redirectAttributes = new MockRedirectAttributes();

        // Act
        String result = petController.processCreationForm(owner, pet, new BindingResult(), redirectAttributes);

        // Assert
        assertEquals(VIEWS_PETS_CREATE_OR_UPDATE_FORM, result);
    }

    @Test
    void testDuplicatePetName() {
        // Arrange
        Pet existingPet = new Pet();
        existingPet.setName("Buddy");
        
        Owner owner = new Owner();
        owner.addPet(existingPet);

        when(ownerRepository.findById(ownerId)).thenReturn(Optional.of(owner));

        Pet newPet = new Pet();
        newPet.setName("Buddy");

        ModelMap model = new ModelMap();
        RedirectAttributes redirectAttributes = new MockRedirectAttributes();

        // Act
        petController.processCreationForm(owner, newPet, new BindingResult(), redirectAttributes);

        // Assert
        verify(owner).getPet("Buddy", true);
    }
}