import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import generated.Owner;
import generated.OwnerRepository;

@ExtendWith(MockitoExtension.class)
class PetControllerTest {

    @Mock
    private OwnerRepository owners;

    @InjectMocks
    private PetController controller;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindOwner_ExistingOwnerId() {
        // Arrange
        int ownerId = 1;
        Owner expectedOwner = new Owner();
        expectedOwner.setId(ownerId);

        when(owners.findById(ownerId)).thenReturn(Optional.of(expectedOwner));

        // Act
        Owner actualOwner = controller.findOwner(ownerId);

        // Assert
        assertEquals(expectedOwner, actualOwner);
        verify(owners).findById(ownerId);
    }

    @Test
    void testFindOwner_NonExistentOwnerId() {
        // Arrange
        int ownerId = 1;
        when(owners.findById(ownerId)).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(IllegalArgumentException.class, () -> controller.findOwner(ownerId));
        verify(owners).findById(ownerId);
    }
}