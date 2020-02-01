var slider = document.getElementById("globalCooldown");
var output = document.getElementById("globalOutput");
var userSlider = document.getElementById("userCooldown");
var userOutput = document.getElementById("userOutput");
output.innerHTML = slider.value + "min"; // Display the default slider value
userOutput.innerHTML = userSlider.value + "min";

// Update the current slider value (each time you drag the slider handle)
slider.oninput = function() {
  output.innerHTML = this.value + "min";
}
userSlider.oninput =function(){
  userOutput.innerHTML = this.value + "min";
}