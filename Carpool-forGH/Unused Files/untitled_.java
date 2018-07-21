int start = matrix.indexOf("distance");

String chopped = matrix.substring(start, start+60);



//this is probably still broken because it loops over "text" distance which can change motherfucker 
//to fix this, we will make a new string that starts at "distance" and ends 100(or a better #
//of characters past distance, then looks for ("value"), then finds the distance in feet