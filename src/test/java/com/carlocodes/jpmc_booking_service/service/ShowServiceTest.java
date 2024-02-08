package com.carlocodes.jpmc_booking_service.service;

import com.carlocodes.jpmc_booking_service.dto.ShowDto;
import com.carlocodes.jpmc_booking_service.entity.Show;
import com.carlocodes.jpmc_booking_service.repository.ShowRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ShowServiceTest {
    @InjectMocks
    private ShowService showService;
    @Mock
    private ShowRepository showRepository;

    @Test
    public void setupTest() throws Exception {
        int rows = 26;
        int numberOfSeatsPerRow = 10;
        int cancellationWindowInMinutes = 2;

        Show show = new Show();
        show.setRows(rows);
        show.setSeatsPerRow(numberOfSeatsPerRow);
        show.setCancellationWindowInMinutes(cancellationWindowInMinutes);

        ReflectionTestUtils.setField(showService, "maxRows", 26);
        ReflectionTestUtils.setField(showService, "maxSeatsPerRow", 10);
        when(showRepository.save(any())).thenReturn(show);

        ShowDto showDto = showService.setup(rows, numberOfSeatsPerRow, cancellationWindowInMinutes);

        assertNotNull(showDto);
        assertEquals(show.getRows(), showDto.getRows());
        assertEquals(show.getSeatsPerRow(), showDto.getSeatsPerRow());
        assertEquals(show.getCancellationWindowInMinutes(), showDto.getCancellationWindowInMinutes());

        verify(showRepository, times(1)).save(any());
    }

    @Test
    public void setupWithExceedingRows() {
        int rows = 27;
        int numberOfSeatsPerRow = 10;
        int cancellationWindowInMinutes = 2;

        Exception exception = assertThrows(Exception.class, () -> showService.setup(rows, numberOfSeatsPerRow, cancellationWindowInMinutes));

        String expectedMessage = "Show setup failed: Exceeded maximum rows or seats per row.";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void setupWithExceedingNumberOfSeatsPerRow() {
        int rows = 26;
        int numberOfSeatsPerRow = 11;
        int cancellationWindowInMinutes = 2;

        Exception exception = assertThrows(Exception.class, () -> showService.setup(rows, numberOfSeatsPerRow, cancellationWindowInMinutes));

        String expectedMessage = "Show setup failed: Exceeded maximum rows or seats per row.";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void findByIdTest() {
        long id = 1L;
        int rows = 26;
        int numberOfSeatsPerRow = 10;
        int cancellationWindowInMinutes = 2;

        Show show = new Show();
        show.setRows(rows);
        show.setSeatsPerRow(numberOfSeatsPerRow);
        show.setCancellationWindowInMinutes(cancellationWindowInMinutes);

        when(showRepository.findById(anyLong())).thenReturn(Optional.of(show));

        Optional<Show> optionalShow = showService.findById(id);

        assertTrue(optionalShow.isPresent());
        assertNotNull(optionalShow.get());
        assertEquals(show.getRows(), optionalShow.get().getRows());
        assertEquals(show.getSeatsPerRow(), optionalShow.get().getSeatsPerRow());
        assertEquals(show.getCancellationWindowInMinutes(), optionalShow.get().getCancellationWindowInMinutes());

        verify(showRepository, times(1)).findById(anyLong());
    }

    @Test
    public void findByIdOptionalEmptyTest() {
        long id = 999L;

        when(showRepository.findById(anyLong())).thenReturn(Optional.empty());

        Optional<Show> optionalShow = showService.findById(id);

        assertTrue(optionalShow.isEmpty());

        verify(showRepository, times(1)).findById(anyLong());
    }
}
