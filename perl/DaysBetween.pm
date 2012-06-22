#!/usr/bin/perl

package DDG::Goodie::DaysBetween;
#ABSTRACT: Calculates the number of days in between two dates.

use DDG::Goodie;
use Date::Calc;

triggers start => 'days between';

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
		return abs($days2 - $days1);
	}
	return;
};

1;
