using System;
using System.Collections.Generic;
using System.Globalization;
using System.IO;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Xml;
using System.Xml.Linq;
using System.ComponentModel;
using System.Drawing;

namespace PewPewLevelEditor
{
    enum Theme
    {
        /// <summary>
        /// Bright sunny day
        /// </summary>
        OutsideDay,

        /// <summary>
        /// Still outside. Night time.
        /// </summary>
        OutsideDark,

        /// <summary>
        /// Dawn. Orange glow from the bottom
        /// </summary>
        OutsideDawn,

        /// <summary>
        /// Like night time, just with purple highlights and different background music
        /// </summary>
        Cave,

        /// <summary>
        /// Exactly like OutsideDawn except with different music
        /// </summary>
        Finish
    }

    class Level
    {
        [Browsable(false)]
        public List<Block> Blocks;

        [Browsable(false)]
        public int Height = 17;

        [Browsable(false)]
        public int Width = 20;

        [Browsable(true)]
        public Theme ColorScheme { get; set; }

        public Level()
        {
            Blocks = new List<Block>();
            ColorScheme = Theme.OutsideDawn;
        }

        public void Save(string file)
        {
            var settings = new XmlWriterSettings
            {
                IndentChars = "\t",
                Indent = true,
                NewLineChars = Environment.NewLine,
                NewLineOnAttributes = true
            };

            using (var xml = XmlWriter.Create(file, settings))
            {

                xml.WriteStartDocument();
                xml.WriteStartElement("level");

                xml.WriteElementString("width", Width.ToString(CultureInfo.InvariantCulture));
                xml.WriteElementString("height", Height.ToString(CultureInfo.InvariantCulture));
                xml.WriteElementString("theme", ((int)ColorScheme).ToString(CultureInfo.InvariantCulture));

                xml.WriteStartElement("blocks");
                foreach (var block in Blocks)
                {
                    xml.WriteStartElement(block.Namespace.Trim());

                    xml.WriteElementString("tilecollision", ((int)block.Collision).ToString(CultureInfo.InvariantCulture));
                    xml.WriteElementString("zposition", ((int)block.ZPosition).ToString(CultureInfo.InvariantCulture));

                    xml.WriteElementString("x", block.X.ToString(CultureInfo.InvariantCulture));
                    xml.WriteElementString("y", block.Y.ToString(CultureInfo.InvariantCulture));

                    xml.WriteElementString("name", block.Name.Trim());


                    if (block.Extras.Count > 0)
                    {
                        xml.WriteStartElement("extras");
                        foreach (var extra in block.Extras)
                        {
                            xml.WriteElementString(extra.Key, extra.Value);
                        }
                        xml.WriteEndElement();
                    }

                    xml.WriteEndElement();
                }
                xml.WriteEndElement();
                xml.WriteEndElement();

                xml.WriteEndDocument();

            }
        }

        public bool Load(string file)
        {
            Blocks = new List<Block>();
            try
            {
                using (var reader = XmlReader.Create(file))
                {
                    XDocument document = XDocument.Load(reader);
                    var levelElement = document.Element("level");

                    if (levelElement != null)
                    {
                        Width = int.Parse(levelElement.Element("width").Value);
                        Height = int.Parse(levelElement.Element("height").Value);

                        var themeElement = levelElement.Element("theme");
                        if (themeElement != null)
                            ColorScheme = (Theme)int.Parse(themeElement.Value);
                        else ColorScheme = Theme.OutsideDawn;

                        var blockElement = levelElement.Element("blocks");

                        #region Tiles
                        foreach (var blk in blockElement.Elements("block"))
                        {

                            int x = int.Parse(blk.Element("x").Value);
                            int y = int.Parse(blk.Element("y").Value);

                            int enumIndex = int.Parse(blk.Element("tilecollision").Value);
                            TileCollision collision = (TileCollision)enumIndex;

                            enumIndex = int.Parse(blk.Element("zposition").Value);
                            ZPosition zpos = (ZPosition)enumIndex;

                            string id = blk.Element("name").Value;

                            Block newBlock = new Block(id, "block", Block.FromFile(id), x, y, collision, zpos);

                            List<Extra> extras = blk.Elements("extras").Select(el => new Extra(el.Name.LocalName, el.Value.Trim())).ToList();

                            newBlock.Extras = extras;

                            Blocks.Add(newBlock);
                        }
                        #endregion

                        #region Gems/Coins
                        foreach (var blk in blockElement.Elements("gem"))
                        {

                            int x = int.Parse(blk.Element("x").Value);
                            int y = int.Parse(blk.Element("y").Value);


                            Block newBlock = new Block(Block.CoinGold, x, y);

                            var extrasElement = blk.Element("extras");
                            List<Extra> extras = new List<Extra>();
                            if (extrasElement != null)
                            {
                                extras.AddRange(extrasElement.Elements().Select(extra => new Extra(extra.Name.LocalName, extra.Value.Trim())));
                            }
                            newBlock.Extras = extras;

                            Blocks.Add(newBlock);

                        }
                        #endregion

                        #region Fly Enemies
                        foreach (var blk in blockElement.Elements("fly"))
                        {

                            int x = int.Parse(blk.Element("x").Value);
                            int y = int.Parse(blk.Element("y").Value);

                            Block newBlock = new Block(Block.Fly, x, y);

                            var extrasElement = blk.Element("extras");
                            List<Extra> extras = new List<Extra>();
                            if (extrasElement != null)
                            {
                                extras.AddRange(extrasElement.Elements().Select(extra => new Extra(extra.Name.LocalName, extra.Value.Trim())));
                            }
                            newBlock.Extras = extras;

                            Blocks.Add(newBlock);
                        }
                        #endregion

                        #region Slime Enemies
                        foreach (var blk in blockElement.Elements("slime"))
                        {

                            int x = int.Parse(blk.Element("x").Value);
                            int y = int.Parse(blk.Element("y").Value);

                            Block newBlock = new Block(Block.Slime, x, y);

                            var extrasElement = blk.Element("extras");
                            List<Extra> extras = new List<Extra>();
                            if (extrasElement != null)
                            {
                                extras.AddRange(extrasElement.Elements().Select(extra => new Extra(extra.Name.LocalName, extra.Value.Trim())));
                            }
                            newBlock.Extras = extras;

                            Blocks.Add(newBlock);
                        }
                        #endregion

                        #region Player

                        var playerElement = blockElement.Element("player");

                        if (playerElement != null)
                        {
                            int x = int.Parse(playerElement.Element("x").Value);
                            int y = int.Parse(playerElement.Element("y").Value);

                            Block newBlock = new Block(Block.Player, x, y);

                            var extrasElement = playerElement.Element("extras");
                            List<Extra> extras = new List<Extra>();
                            if (extrasElement != null)
                            {
                                extras.AddRange(extrasElement.Elements().Select(extra => new Extra(extra.Name.LocalName, extra.Value.Trim())));
                            }

                            newBlock.Extras = extras;

                            Blocks.Add(newBlock);
                        }

                        #endregion
                        
                        #region Exit
                        var exitElement = blockElement.Element("exit");

                        if (exitElement != null)
                        {
                            int x = int.Parse(exitElement.Element("x").Value);
                            int y = int.Parse(exitElement.Element("y").Value);

                            Block newBlock = new Block(Block.Exit, x, y);

                            var extrasElement = exitElement.Element("extras");
                            List<Extra> extras = new List<Extra>();
                            if (extrasElement != null)
                            {
                                extras.AddRange(extrasElement.Elements().Select(extra => new Extra(extra.Name.LocalName, extra.Value.Trim())));
                            }
                            newBlock.Extras = extras;

                            Blocks.Add(newBlock);
                        }
                    }

                    #endregion

                }
            }
            catch
            {
                return false;
            }
            return true;

        }

    }
}
