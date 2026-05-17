import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.SpyBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.test.MockitoExtension;

import generated.PetController;

@ExtendWith(MockitoExtension.class)
public class TestPetControllerFindPet {

    @SpyBean
    private PetController petController;

    @Mock
    private OwnerRepository owners;

    @Mock
    private PetTypeRepository types;

    @Autowired
    @InjectMocks
    private PetController controller;

    @BeforeEach
    void setUp() {
        // Reset all mocks and spies before each test
        reset(owners);
        reset(controller);
    }

    @Test
    public void testFindPetWithNullPetId() {
        when(owners.findById(anyInt())).thenReturn(Optional.of(mock(Owner.class)));
        
        Pet result = controller.findPet(1, null);

        assertEquals(result.getClass(), Pet.class);
        assertNotSame(result, new Pet());
    }

    @Test
    public void testFindPetWhenOwnerNotFound() {
        when(owners.findById(anyInt())).thenThrow(new IllegalArgumentException("Owner not found"));
        
        assertThrows(IllegalArgumentException.class, () -> {
            controller.findPet(1, 1);
        });
    }

    @Test
    public void testFindPetWhenPetExists() {
        Owner owner = mock(Owner.class);
        Pet pet = new Pet();
        when(owners.findById(anyInt())).thenReturn(Optional.of(owner));
        when(owner.getPet(anyInt())).thenReturn(pet);
        
        Pet result = controller.findPet(1, 1);

        assertEquals(pet, result);
    }

    @Test
    public void testFindPetWhenPetDoesNotExist() {
        Owner owner = mock(Owner.class);
        when(owners.findById(anyInt())).thenReturn(Optional.of(owner));
        when(owner.getPet(anyInt())).thenReturn(null);
        
        Pet result = controller.findPet(1, 1);

        assertNull(result);
    }
}