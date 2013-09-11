using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Globalization;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace PewPewLevelEditor
{
    class Extra
    {
        [Browsable(true)]
        public string Key { get; set; }

        [Browsable(true)]
        public string Value { get; set; }

        public Extra(string key, string val)
        {
            Key = key;
            Value = val;
        }

        public Extra(string key, int val)
        {
            Key = key;
            Value = val.ToString(CultureInfo.InvariantCulture);
        }

        public Extra()
        {
            
        }

        public override string ToString()
        {
            return Key;
        }
    }
}
