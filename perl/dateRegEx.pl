#!/usr/bin/perl

use Date::Calc qw(Date_to_Days);



$_ = " 12/12/1212 12/12/1212";

print(daysBetween($_));

sub daysBetween {

	@dates = m#([01]?[0-9])/([0-3]?[0-9])/([0-9]{4}(?=\s|$))#g;

	print(scalar(@dates). " elements\n");

	if(scalar(@dates) == 6) {

		eval {
			$days1 = Date_to_Days(@dates[2], @dates[0], @dates[1]);
			$days2 = Date_to_Days(@dates[5], @dates[3], @dates[4]);
		};
		if ($@) {
			print("error");
			return;
		}
		return abs($days2 - $days1);

#	if(check_date(@dates[2],@dates[1],@dates[0]) 
#		&& check_date(@dates[5],@dates[4],@dates[3])) {
#		$days1 = Date_to_Days($ear1, $month1, $day1);
#		$days2 = Date_to_Days($year2, $month2, $day2);
#		return abs($days2 - $days1);
#	}
	}

	return;
}
