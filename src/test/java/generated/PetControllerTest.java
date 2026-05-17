import generated.PetController;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.ModelMap;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PetControllerTest {
    @Mock
    private OwnerRepository owners;
    @Mock
    private PetTypeRepository types;
    @InjectMocks
    private PetController petController;

    @Test
    public void testFindOwner_Exists() {
        int ownerId = 1;
        Owner mockOwner = new Owner();
        when(owners.findById(ownerId)).thenReturn(Optional.of(mockOwner));

        Owner result = petController.findOwner(ownerId);
        assertSame(mockOwner, result);
    }

    @Test
    public void testFindOwner_NotExists() {
        int ownerId = 1;
        when(owners.findById(ownerId)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> petController.findOwner(ownerId));
    }
}