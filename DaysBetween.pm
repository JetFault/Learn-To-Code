package DDG::Goodie::DaysBetween;
#ABSTRACT: Calculates the number of days in between two dates.

use DDG::Goodie;
use Date::Calc qw(Date_to_Days);

triggers start => 'days';

zci is_cached => 1;

handle remainder => sub {
	@dates = m#([01]?[0-9])/([0-3]?[0-9])/([0-9]{4}(?=\s|$))#g;

	if(scalar(@dates) == 6) {

		eval {
			$days1 = Date_to_Days(@dates[2], @dates[0], @dates[1]);
			$days2 = Date_to_Days(@dates[5], @dates[3], @dates[4]);
		};
		if ($@) {
			return;
		}
		$daysBetween = abs($days2 - $days1);
		return  (/inclusive/) ? $daysBetween + 1 : $daysBetween;
	}
	return;
};

1;
