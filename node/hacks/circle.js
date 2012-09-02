for(var i = 1234567; i <= 9876543; i++) {
  fits_descrip(i, function(mid) {
    if(mid) {
      console.log("Number: " + i + 
        " middle number: "  +  mid);
      process.exit(0);
    }
  });
}

function is_uniq(num, cb) {
  var n = num.toString();
  var uniq = [];
  for(var i = 0; i < n.length; i++) {
    var dig = n[i];
    if(uniq[dig] === 1) {
      cb(false);
      return;
    }
    uniq[dig] = 1;
  }
  cb(true);
}

//Check if multiply
function fits_descrip(num, cb) {
  is_uniq(num, function(uniq) {
    if(uniq) {
      var n = num.toString();
      var first = n[0] * n[1] * n[2];
      var second = n[2] * n[3] * n[4];
      var third = n[4] * n[5] * n[6];
      if(first == second && second == third) {
        cb(n[3]);
      }
    }
    cb(null);
  });
}
