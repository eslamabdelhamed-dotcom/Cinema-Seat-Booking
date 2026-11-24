import java.util.*;

/*
 * TASK: Cinema Seat Booking
 * HashMap with a custom class as the key.
 *
 * Key   = Seat
 * Value = customer's name
 *
 * Run once without equals() and hashCode() (as an experiment), then
 * add/restore the implementations below and run again.
 */

class Task_CinemaBooking {

    // A seat in the cinema hall, for example B5 = row B, seat number 5.
    static class Seat {
        final char row;     // 'A' to 'E'
        final int number;   // 1 to 6

        Seat(char row, int number) {
            this.row = row;
            this.number = number;
        }

        @Override
        public String toString() {
            return "" + row + number;
        }

        // Two seats are equal when both their row and number are equal.
        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }

            Seat seat = (Seat) o;
            return row == seat.row && number == seat.number;
        }

        // Equal seats must have the same hash code.
        @Override
        public int hashCode() {
            return Objects.hash(row, number);
        }
    }

    public static void main(String[] args) {

        // Key = Seat, Value = name of the customer who booked it
        HashMap<Seat, String> bookings = new HashMap<>();
        HashSet<Seat> broken = new HashSet<>();

        // Part A: book these seats using put()
        // A1 -> Ali, A2 -> Sara, B5 -> Omar, C3 -> Mona
        bookings.put(new Seat('A', 1), "Ali");
        bookings.put(new Seat('A', 2), "Sara");
        bookings.put(new Seat('B', 5), "Omar");
        bookings.put(new Seat('C', 3), "Mona");

        System.out.println("Bookings:           " + bookings);

        // Part B: who booked seat C3? Use get().
        System.out.println("Who booked C3:      "
                + bookings.get(new Seat('C', 3)));

        // Part C: are seats A2 and E6 taken? Use containsKey().
        System.out.println("Is A2 taken:        "
                + bookings.containsKey(new Seat('A', 2)));
        System.out.println("Is E6 taken:        "
                + bookings.containsKey(new Seat('E', 6)));

        // Part D: Youssef wants B5. Do not replace an existing booking.
        String existingCustomer =
                bookings.putIfAbsent(new Seat('B', 5), "Youssef");

        if (existingCustomer == null) {
            System.out.println("Youssef -> B5:      booked");
        } else {
            System.out.println("Youssef -> B5:      refused, taken by "
                    + existingCustomer);
        }

        // Part E: Ali moves from A1 to D2. Use remove() and put().
        String ali = bookings.remove(new Seat('A', 1));
        if (ali != null) {
            bookings.put(new Seat('D', 2), ali);
        }

        System.out.println("After Ali moved:    " + bookings);

        // Part F: seats E1 and E2 are broken. Hana wants E1.
        broken.add(new Seat('E', 1));
        broken.add(new Seat('E', 2));

        if (broken.contains(new Seat('E', 1))) {
            System.out.println("Hana -> E1:         refused, seat is broken");
        } else {
            bookings.put(new Seat('E', 1), "Hana");
            System.out.println("Hana -> E1:         booked");
        }

        // Part G: how many seats are booked? Use size().
        System.out.println("Seats booked:       " + bookings.size());

        // GIVEN - prints the seating chart. Do not change printChart().
        System.out.println();
        printChart(bookings, broken);
    }

    // GIVEN - do not change. Prints the hall:
    // X = booked, # = broken, . = free
    static void printChart(HashMap<Seat, String> bookings,
                           HashSet<Seat> broken) {
        System.out.println("     1  2  3  4  5  6");
        for (char row = 'A'; row <= 'E'; row++) {
            System.out.print(row + "  ");
            for (int number = 1; number <= 6; number++) {
                Seat seat = new Seat(row, number);
                if (bookings.containsKey(seat)) {
                    System.out.print("  X");
                } else if (broken.contains(seat)) {
                    System.out.print("  #");
                } else {
                    System.out.print("  .");
                }
            }
            System.out.println();
        }
    }
}

/*
 * QUESTIONS - Answers
 *
 * Q1. Why did we have to override hashCode() and equals()?
 * Answer:
 * By default, Java's Object equals() compares object identity, not the
 * seat's row and number. HashMap and HashSet use hashCode() to find a
 * bucket and equals() to identify a matching key. Overriding both makes
 * two separately-created Seat objects representing the same seat behave
 * as the same key. The hashCode/equals contract requires equal objects
 * to have the same hash code.
 *
 * Q2. Delete hashCode() but keep equals(). What happens? Why?
 * Answer:
 * The program may fail to find a logically equal Seat, even though equals()
 * returns true. The inherited Object.hashCode() can give the two Seat
 * objects different hash codes, so HashMap/HashSet may search different
 * buckets. Overriding equals() requires a matching hashCode() override.
 *
 * Q3. Why are row and number final? What could go wrong if a booked seat
 *     could be changed after it was put in the map?
 * Answer:
 * They describe the identity of the seat and should not change. If a field
 * used by equals() and hashCode() changed after insertion, the object's
 * hash code could change while it remains in its old bucket. The map/set
 * might then fail to find or remove that object correctly.
 *
 * Q4. Change equals(Object o) to equals(Seat o). What happens?
 *     Then also remove @Override and run again. What happens now?
 * Answer:
 * With @Override, compilation fails because equals(Seat) does not override
 * Object.equals(Object). If @Override is removed, it becomes an overload,
 * not an override. HashMap/HashSet use equals(Object), so the inherited
 * identity comparison is used and separately-created matching seats are
 * not treated as equal.
 *
 * BONUS. Replace the whole Seat class with:
 *     record Seat(char row, int number) {}
 * Answer:
 * A record automatically provides equals(), hashCode(), and toString()
 * based on its components, so separately-created Seat records with the
 * same row and number work as equal keys. However, the generated toString()
 * format is "Seat[row=A, number=1]", not "A1", so the displayed map format
 * differs unless a custom toString() is supplied.
 */
