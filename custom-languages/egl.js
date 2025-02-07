hljs.registerLanguage('egl', function(hljs) {
    return {
      case_insensitive: false,
      contains: [
        {
          className: 'comment',
          begin: '\\[\\*', end: '\\*\\]',
          relevance: 10
        },
        {
          className: 'template-tag',
          begin: '\\[%', end: '%\\]',
          relevance: 10,
          subLanguage: 'eol'
        }
      ]
    };
  });
