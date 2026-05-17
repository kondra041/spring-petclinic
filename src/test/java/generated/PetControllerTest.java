package generated;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.yourpackage.Owner;
import com.yourpackage.OwnerRepository;
import com.yourpackage.Pet;
import com.yourpackage.PetType;
import com.yourpackage.PetTypeRepository;
import com.yourpackage.PetValidator;
import com.yourpackage.StringUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
class PetControllerTests {

    private OwnerRepository ownerRepository;
    private PetTypeRepository petTypeRepository;
    private PetController petController;

    @BeforeEach
    void setUp() {
        ownerRepository = mock(OwnerRepository.class);
        petTypeRepository = mock(PetTypeRepository.class);
        petController = new PetController(ownerRepository, petTypeRepository);
    }

    @Test
    void testFindOwner() {
        int ownerId = 1;
        Owner expectedOwner = new Owner();
        when(ownerRepository.findById(ownerId)).thenReturn(Optional.of(expectedOwner));

        Owner actualOwner = petController.findOwner(ownerId);

        assertEquals(expectedOwner, actualOwner);
    }

    @Test
    void testFindOwnerNotFound() {
        int ownerId = 2;
        when(ownerRepository.findById(ownerId)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> {
            petController.findOwner(ownerId);
        });
    }
}