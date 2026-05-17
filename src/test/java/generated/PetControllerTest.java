package generated;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

public class PetControllerTest {

    private OwnerRepository ownerRepository;
    private PetTypeRepository petTypeRepository;
    private PetController petController;

    @BeforeEach
    public void setUp() {
        ownerRepository = mock(OwnerRepository.class);
        petTypeRepository = mock(PetTypeRepository.class);
        petController = new PetController(ownerRepository, petTypeRepository);
    }

    @Test
    public void testFindPetWithNullPetId() {
        int ownerId = 1;
        Owner owner = new Owner();
        when(ownerRepository.findById(ownerId)).thenReturn(Optional.of(owner));

        Pet pet = petController.findPet(ownerId, null);

        assertEquals(new Pet(), pet);
    }

    @Test
    public void testFindPetWithNonExistentOwner() {
        int ownerId = 1;
        when(ownerRepository.findById(ownerId)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> {
            petController.findPet(ownerId, 2);
        });
    }

    @Test
    public void testFindPetWithExistingOwnerAndPet() {
        int ownerId = 1;
        Owner owner = new Owner();
        Pet existingPet = new Pet();
        existingPet.setId(2);

        when(ownerRepository.findById(ownerId)).thenReturn(Optional.of(owner));
        when(owner.getPet(2)).thenReturn(existingPet);

        Pet pet = petController.findPet(ownerId, 2);

        assertEquals(existingPet, pet);
    }
}