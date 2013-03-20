(function($)
{ 
	//We'll name our plugin "jConfirm"
	$.fn.jConfirm=function(options)
	{
		//Defaults values
		var defaults=
		{
			'validText': 'OK',
			'cancelText': 'Cancel',
			'title': 'Are you sure?',
			'message': 'Do you really want to do that?',
			'splash': true,
			'onconfirm': null,
			'oncancel': null,
			'onclick': null,
			'onopen': null,
			'onclose': null
		}; 
		var parameters=$.extend(defaults, options); 
		
		if(parameters.splash)
		{
			//If the splash screen is not created, we add it
			var splash=$('#jConfirmSplash');
			if(splash.length==0)
			{
				splash=$('<div id="jConfirmSplash">');
				splash.css(
				{
					'position': 'fixed',
					'width': '100%',
					'height': '100%',
					'top': '0px',
					'left': '0px',
					'opacity': 0.7,
					'z-index': 999999
				}).hide().appendTo($('body'));
			}
		}
		
		//Plugin code
		return this.each(function()
		{
			var link=$(this);			
			
			//If we click on the element
			link.click(function(clickEvent)
			{
				clickEvent.preventDefault();
				
				//We create the confirm window
				var confirmWindow=$('<div></div>');
				confirmWindow.addClass('jConfirmWindow').css(
				{
					'position': 'fixed',
					'left': '50%',
					'top': '50%',
					'z-index': 1000000
				}).hide().appendTo($('body'));
				
				var title=$('<h1>');
				title.addClass('jConfirmTitle').html(parameters.title).appendTo(confirmWindow);
				
				var message=$('<div>');
				message.addClass('jConfirmMessage').html(parameters.message).appendTo(confirmWindow);
				
				//Only for checking
				confirmWindow.css(
				{
					'width': '300px',
					'height': '300px',
					'background-color': '#000',
					'color': '#fff'
				});
				splash.css('background-color','#000');
				//End of checking code
				
				//Let's center the confirm Window
				confirmWindow.css(
				{
					'margin-left': '-'+(confirmWindow.outerWidth())/2+'px',
					'margin-top': '-'+(confirmWindow.outerHeight())/2+'px'
				});
				
							
				//Function called when the confirm window is closed
				function jConfirmWindowClosed(callbackFunction)
				{
					//User can't click on buttons anymore
					confirmWindow.find('input').attr('disabled','true');
					if(callbackFunction)
					{
						callbackFunction(link);
					}
					
					if(parameters.splash)
					{
						splash.fadeOut('slow');
					}
					confirmWindow.fadeOut('slow',function()
					{
						if(parameters.onclose)
						{
							parameters.onclose(link);
						}
						$(this).remove();
					});
				}
				
				//Buttons container
				var buttonsContainer=$('<p>');
				buttonsContainer.addClass('jConfirmButtonsContainer').appendTo(confirmWindow);

				//Adding the OK button
				var validButton=$('<input/>');
				validButton.attr('type','button').attr('value',parameters.validText)
				.addClass('jConfirmValidButton').click(function()
				{
					jConfirmWindowClosed(parameters.onconfirm);
				}).appendTo(buttonsContainer);
				
				//Adding the Cancel button
				var cancelButton=$('<input/>');
				cancelButton.attr('type','button').attr('value',parameters.cancelText)
				.addClass('jConfirmCancelButton').click(function()
				{
					jConfirmWindowClosed(parameters.oncancel);
				}).appendTo(buttonsContainer);
				
				if(parameters.onclick)
				{
					if(parameters.onclick(link)===false)
					{
						confirmWindow.remove();
						return false;
					}
				}

				if(parameters.splash)
				{
					splash.fadeIn('slow');
				}
				
				confirmWindow.fadeIn('slow',function()
				{
					if(parameters.onopen)
					{
						parameters.onopen(link);
					}
				});
			});
		});						   
	};
})(jQuery);