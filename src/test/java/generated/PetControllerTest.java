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

import generated.PetController;
import generated.OwnerRepository;
import generated.PetTypeRepository;
import generated.Owner;
import generated.Pet;
import generated.PetType;

public class PetControllerTest {

    private OwnerRepository ownersMock;
    private PetTypeRepository typesMock;
    private PetController controller;

    @BeforeEach
    public void setUp() {
        ownersMock = mock(OwnerRepository.class);
        typesMock = mock(PetTypeRepository.class);
        controller = new PetController(ownersMock, typesMock);
    }

    @Test
    public void testFindPet_WhenPetIdIsNull_ReturnsNewPet() {
        int ownerId = 1;
        Integer petId = null;

        ModelMap modelMap = new ModelMap();
        when(ownersMock.findById(ownerId)).thenReturn(Optional.of(new Owner()));

        Pet result = controller.findPet(ownerId, petId);

        assertNotNull(result);
        assertTrue(result.isNew());
    }

    @Test
    public void testFindPet_WhenPetIdIsNotNull_ReturnsExistingPet() {
        int ownerId = 1;
        Integer petId = 2;
        Pet existingPet = new Pet();
        existingPet.setId(petId);

        when(ownersMock.findById(ownerId)).thenReturn(Optional.of(new Owner()));
        when(ownersMock.findById(ownerId).get().getPet(petId)).thenReturn(existingPet);

        ModelMap modelMap = new ModelMap();
        Pet result = controller.findPet(ownerId, petId);

        assertNotNull(result);
        assertEquals(existingPet.getId(), result.getId());
    }

    @Test
    public void testFindPet_WhenOwnerDoesNotExist_ThrowsIllegalArgumentException() {
        int ownerId = 1;
        Integer petId = null;

        when(ownersMock.findById(ownerId)).thenReturn(Optional.empty());

        ModelMap modelMap = new ModelMap();
        assertThrows(IllegalArgumentException.class, () -> controller.findPet(ownerId, petId));
    }
}