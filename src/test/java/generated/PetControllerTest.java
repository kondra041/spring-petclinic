package generated;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import generated.PetController; // assuming the source code is in the same package as the test
import generated.OwnerRepository;
import generated.PetTypeRepository;
import generated.Owner;
import generated.Pet;

@ExtendWith(MockitoExtension.class)
public class PetControllerTest {

    @InjectMocks
    private PetController petController;

    @Mock
    private OwnerRepository owners;

    @Mock
    private PetTypeRepository types;

    @Mock
    private ModelMap model;

    @Mock
    private RedirectAttributes redirectAttributes;

    @Test
    public void testFindPet_WhenPetIdIsNull() {
        int ownerId = 1;
        Owner owner = new Owner();
        when(owners.findById(ownerId)).thenReturn(Optional.of(owner));

        Pet result = petController.findPet(ownerId, null);

        assertNotNull(result);
        verify(owners).findById(ownerId);
    }

    @Test
    public void testFindPet_WhenOwnerNotFound() {
        int ownerId = 1;
        when(owners.findById(ownerId)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> petController.findPet(ownerId, 2));
    }
}