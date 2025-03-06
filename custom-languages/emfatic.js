hljs.registerLanguage('emfatic', function(hljs) {
    return {
      case_insensitive: false,
      contains: [
        {
          className: 'comment',
          begin: '/\\*', end: '\\*/'
        },
        {
          className: 'comment',
          begin: '//', end: '$'
        },
        {
            className: 'comment',
            begin: '@\\w+', end: '(?=\\()|$'
          },
        {
          className: 'number',
          begin: '\\b\\d+(\\.\\d+)?\\b'
        },
        {
          className: 'keyword',
          begin: '\\b(abstract|attr|class|enum|extends|import|package|ref|val|op|readonly|volatile|transient|unsettable|derived|unique|ordered|resolve|id)\\b'
        },
        {
          className: 'string',
          begin: '\'', end: '\''
        },
        {
          className: 'string',
          begin: '"', end: '"'
        },
        {
          className: 'type',
          begin: '\\b(boolean|Boolean|byte|Byte|char|Character|double|Double|float|Float|int|Integer|long|Long|short|Short|Date|String|Object|Class|EObject|EClass)\\b'
        }
      ]
    };
  });