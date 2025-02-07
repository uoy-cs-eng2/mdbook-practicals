hljs.registerLanguage('eol', function(hljs) {
    return {
      case_insensitive: false,
      contains: [
        {
          className: 'comment',
          begin: '/\\*', end: '\\*/',
          contains: [
            {
              className: 'doctag',
              begin: '@', end: '$'
            }
          ]
        },
        {
          className: 'comment',
          begin: '//', end: '$'
        },
        {
          className: 'doctag',
          begin: '@', end: '$'
        },
        {
          className: 'keyword',
          begin: '\\b(true|false|self|loopCount|hasMore)\\b'
        },
        {
          className: 'number',
          begin: '\\b\\d+(\\.\\d+)?\\b'
        },
        {
          className: 'keyword',
          begin: '\\b(import|driver|alias|if|switch|case|default|operation|function|new|else|for|var|return|async|break|breakAll|and|or|not|xor|implies|ext|in|continue|while|throw|delete|transaction|abort|model|group|as)\\b'
        },
        {
          className: 'operator',
          begin: '[!<>:]?=|<>|<|>|\\+|(?<!\\.)\\*|-|(?<!^)/|@@|\\|\\|'
        },
        {
          className: 'string',
          begin: '"', end: '"'
        }
      ]
    };
  });
