import javax.swing.SwingUtilities;

import model.SesiEvent;
import model.Tiket;
import service.BookingService;
import view.MainFrame;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            BookingService bookingService = new BookingService();

            Tiket vip = new Tiket("T001", "VIP", 150000, 50, false);
            Tiket allDay = new Tiket("T002", "All Day", 100000, 100, false);
            Tiket perSesi = new Tiket("T003", "Per-Sesi", 50000, 0, true);

            perSesi.tambahSesi(new SesiEvent("S001", "Sesi 1", 40));
            perSesi.tambahSesi(new SesiEvent("S002", "Sesi 2", 40));
            perSesi.tambahSesi(new SesiEvent("S003", "Sesi 3", 40));
            perSesi.tambahSesi(new SesiEvent("S004", "Sesi 4", 40));

            bookingService.tambahTiket(vip);
            bookingService.tambahTiket(allDay);
            bookingService.tambahTiket(perSesi);

            MainFrame frame = new MainFrame(bookingService);
            frame.setVisible(true);
        });
    }
}
