
var safe = jQuery.noConflict();
console.log("jQuery version injected is: " + safe.fn.jquery);

safe.fn.bindFirst = function(name, fn) {
  var elem, handlers, i, _len;
  this.bind(name, fn);
  for (i = 0, _len = this.length; i < _len; i++) {
    elem = this[i];
    handlers = jQuery._data(elem).events[name.split('.')[0]];
    handlers.unshift(handlers.pop());
  }
};

safe(document).ready(function() {
	  safe("a,submit,button,input[type='image'],input[type='submit']").bindFirst("click", function( event ) {
		log(event);
	  });
	  safe("input[type='text'], textarea, input[type='password']").bindFirst("blur", function( event ) {
		log(event);
	  });
	  safe("select,radio,checkbox,a,input[type='radio'],input[type='checkbox']").bindFirst("change", function( event ) {
		log(event);
	  });
	  safe(":button").bindFirst("click", function( event ) {
		log(event);
	  });
  });

function log(event) {
	var message="";
	var isCheckbox = safe(event.target).is(":checkbox");
	var isRadio = safe(event.target).is(":radio");
	var isText = safe(event.target).is(":text");
	var isImage = safe(event.target).is(":image");
	var isButton = safe(event.target).is(":button");
	var isSubmit = safe(event.target).is(":submit");
	var isSelect = event.target.tagName=="SELECT" || event.target.tagName=="select" || event.target.tagName=="select-one";
	var isAnchor = safe(event.target).is('a');				
	var type="type:"+event.target.type+"TAB";
	if (isSubmit || isButton) {
		console.log('may cause url change');
	} 
	console.log(event.target.tagName + ' >> ' + getXPath(event.target));
	console.log(event.target.value);
	var message = "FINDER[id=" + event.target.id + ";name=" + event.target.name;
	if (isCheckbox){
		message += "value:" + event.target.checked;
	} else if (isRadio){
		message += "value:" + event.target.checked;
	} else if (isSubmit || isButton || isImage){
		message += "value:" + safe(event.target).attr("value");
	} else if (isAnchor) {
		message = "FINDER[tag=a;id=" + event.target.id + ";value=" + event.target.value;
		message = message + ";href=" + safe(event.target).attr('href');
		message = message + ";value=" + safe(event.target).text() + "]";
	} else if(isSelect){
		message += "value:"+ safe("option:selected", event.target).text()+"("+event.target.value+")";	
		options = "";
	    if (event.target.tagName){
	        selector = event.target.tagName+"[name='"+event.target.name+"']";
	    } else if (event.target.id){
	        selector = "#"+event.target.id+" ";
	    }
	    safe(selector+" option").each(function () {
	            options += safe(this).text() + ";";
	  	});
		if(options){
			message += "TAB options:"+options;
		}
	} else{
	    message += "value:" + event.target.value;		    
	    textElementName = event.target.name;
	    alert('text' + event.target.value);
	    textEvents.push(event);	
	    return;   
	}

	message = message + "]";
	informServer(message);
	if(isButton || isSubmit){
		ineffectiveDelay(300);
	}

}

var textFlag=0;
var textElementName="";
setInterval(function(){clock()},1000);
var textEvents=new Array(); 
function clock() {
	while(textEvents.length>0){
		var event=textEvents.pop();
		var position = getPosition(event);
		console.log("The position is :" + position); 
		var message =position+"attid:"+safe(event.target).attr('attid')+"TAB"+ "url:"+safe(location).attr('href')+"TAB"+"type:"+event.target.type+"TAB"+"id:"+event.target.id+"TAB"+"name:"+event.target.name+"TAB"+"event:"+event.type+"TAB"+"value:"+event.target.value;		
		informServer(message);
	}
}

function getXPath( element )
{
    var xpath = '';
    for ( ; element && element.nodeType == 1; element = element.parentNode )
    {
        var id = safe(element.parentNode).children(element.tagName).index(element) + 1;
        id > 1 ? (id = '[' + id + ']') : (id = '');
        xpath = '/' + element.tagName.toLowerCase() + id + xpath;
    }
    return xpath;
}


 function informServer(message){

	 message=message.replace(/%/g, 'PERCENTAGE');
 
	 console.log('Sending Event: ' + message);
	 
	 safe.ajax({
		    type: "post", url: "http://localhost:8188/"+message,
		    success: function (data, text) {
		    	console.log("success");
		    },
		    error: function (request, status, error) {
		    	console.log(request.responseText);
		    	//safe.get("http://localhost:8188/"+message);
		    }
		});
}

function ineffectiveDelay(ms) {
	    var start = +(new Date());
	    while (new Date() - start < ms);
}

function getPosition(event){
	var position ="top:"+ safe(event.target).position().top+"TAB"+"left:"+ safe(event.target).position().left+"TABwidth:"+safe(event.target).width()+"TABheight:"+safe(event.target).height()+"TAB"+"tag:"+safe(event.target).prop('tagName')+"TAB";
	return position;
}
