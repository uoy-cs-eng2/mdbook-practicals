hljs.registerLanguage('egx', function(hljs) {
    var eol = hljs.getLanguage('eol'); // Get the existing EOL language definition
  
    return hljs.inherit(eol, {
      keywords: {
        keyword: 'transform rule guard pre post target extends parameters template overwrite protectRegions merge append patch'
      }
    });
  });