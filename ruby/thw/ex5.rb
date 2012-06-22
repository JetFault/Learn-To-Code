my_name = "Jerry Reptak"
my_age = 21 #Earth years
my_height = 71 #inches
my_weight = 150 #pounds
my_eyes = 'Brown'
my_teeth = 'White'
my_hair = 'Dirty Blonde'

puts "Let's talk about %s." % my_name
puts "He's %d inches tall and weights %d pounds." % [my_height, my_weight]
puts "He's got %s eyes and %s teeth." % [my_eyes, my_teeth]
puts "His hair color is %s and it's beautiful!" % my_hair

puts "If I add %d, %d, and %d I get %d." % [
	my_age, my_height, my_weight, my_age + my_height + my_weight]
