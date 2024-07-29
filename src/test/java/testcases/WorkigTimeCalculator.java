package testcases;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Scanner;

import org.testng.Assert;
import org.testng.annotations.Test;

public class WorkigTimeCalculator {
	public static void main(String[] args) {

		System.out.println("Enter the last 'start time' this format and press ENTER (dd-MM-yyyy hh:mm ):  ");
		Scanner scanner = new Scanner(System.in);

		// Input start time in the correct format "dd-MM-yyyy hh:mm a"
		String startTimeStr = scanner.nextLine();

		// Parse the input string to LocalDateTime for start time
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm a", Locale.US);
		LocalDateTime startTime = LocalDateTime.parse(startTimeStr, formatter);

		// Get the current time
		LocalDateTime currentTime = LocalDateTime.now();

		// Already completed time in the format "8 hours 9 minutes"
		System.out.println("Enter already completed time (hours minutes): and press ENTER ");
		String completedTimeStr = scanner.nextLine();
		Duration completedTime = parseDuration(completedTimeStr);

		// Calculate the elapsed time from start time to current time
		Duration totalDuration = Duration.between(startTime, currentTime).plus(completedTime);

		// Calculate hours and minutes from the total duration
		long totalHours = totalDuration.toHours();
		long totalMinutes = totalDuration.minusHours(totalHours).toMinutes();

		System.out.println("Total Working Time: " + totalHours + " hours " + totalMinutes + " minutes");
		scanner.close();
	}

	private static Duration parseDuration(String durationStr) {
		String[] parts = durationStr.split("\\s");
		long hours = 0;
		long minutes = 0;

		for (int i = 0; i < parts.length; i++) {
			if ("hours".equalsIgnoreCase(parts[i]) && i > 0) {
				hours = Long.parseLong(parts[i - 1]);
			} else if ("minutes".equalsIgnoreCase(parts[i]) && i > 0) {
				minutes = Long.parseLong(parts[i - 1]);
			}
		}

		return Duration.ofHours(hours).plusMinutes(minutes);
	}
	
	

}
