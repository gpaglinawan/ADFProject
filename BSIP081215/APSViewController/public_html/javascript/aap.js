function onloadaapPage(event){
    document.onkeydown = function (e){
           var evt = e?e:window.event;
           var keycode = evt.keyCode;
           var tag = evt.srcElement.tagName;
           
           if (keycode == 8){
                if (tag == 'INPUT'){
                    return true;
                }
                else{
                    return false;
                }
           }
           else{
                return true;
           }
    
    };

}

function closeWindow()
{

    window.close();
}