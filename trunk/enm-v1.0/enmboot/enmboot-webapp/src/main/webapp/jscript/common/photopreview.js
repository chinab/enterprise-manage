
function preview5(){
var x = document.getElementById("file5");
 if(!x || !x.value) return;
var patn = /\.jpg$|\.jpeg$|\.gif$/i;
if(patn.test(x.value)){ 
var y = document.getElementById("img5");
if(y){
y.src = "file://localhost/" + x.value;
}else{
var img=document.createElement("img");      img.setAttribute("src","file://localhost/"+x.value);
img.setAttribute("width","120");
img.setAttribute("height","90");
img.setAttribute("id","img5");
document.getElementById("form5").appendChild(img);
 }
}else{
alert("您选择的似乎不是图像文件。");
}}
