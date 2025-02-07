hljs.registerLanguage('etl', function(hljs) {
    var eol = hljs.getLanguage('eol'); // Get the existing EOL language definition
  
    return hljs.inherit(eol, {
      keywords: {
        keyword: 'transform auto guard pre post to extends rule abstract'
      }
    });
  });