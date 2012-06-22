cars = 100
space_in_a_car = 4
drivers = 30
passengers = 90

cars_not_driven = cars - drivers
cars_driven = drivers
carpool_capacity = cars_driven * space_in_a_car
average_passengers_per_car = passengers / cars_driven

puts "There are #{cars} cars available."
puts "There are #{drivers} drivers available."
puts "There will #{cars_not_driven} empty cars."
puts "We can transport #{carpool_capacity} people."
puts "We have #{passengers} passengers to transport."
puts "We need to put about #{average_passengers_per_car} in each car."
  
