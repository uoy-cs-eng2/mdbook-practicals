hljs.registerLanguage('evl', function(hljs) {
    var eol = hljs.getLanguage('eol'); // Get the existing EOL language definition
  
    return hljs.inherit(eol, {
      keywords: {
        keyword: 'context constraint guard pre post assumes critique message title do check fix typeOf kindOf high medium low'
      }
    });
  });