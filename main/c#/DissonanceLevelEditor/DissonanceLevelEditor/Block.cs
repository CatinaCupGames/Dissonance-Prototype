using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace PewPewLevelEditor
{
    /// <summary>
    /// Controls the collision detection and response behavior of a tile.
    /// </summary>
    enum TileCollision
    {
        /// <summary>
        /// A passable tile is one which does not hinder player motion at all.
        /// </summary>
        Passable = 0,

        /// <summary>
        /// An impassable tile is one which does not allow the player to move through
        /// it at all. It is completely solid.
        /// </summary>
        Impassable = 1,

        /// <summary>
        /// A platform tile is one which behaves like a passable tile except when the
        /// player is above it. A player can jump up through a platform as well as move
        /// past it to the left and right, but can not fall down through the top of it.
        /// </summary>
        Platform = 2,


        /// <summary>
        /// Will kill the player if they touch it
        /// </summary>
        Killable = 3,

    }

    enum ZPosition
    {
        /// <summary>
        /// Player will not interact with tile
        /// </summary>
        Behind,

        /// <summary>
        /// Player is still infront of it, however, it will be processed when updating physics
        /// </summary>
        OnLevel,


        /// <summary>
        /// Player will not interact with tile
        /// </summary>
        InFront,
    }


    class Block
    {

        [Browsable(true)]
        public Bitmap Image { get; set; }

        [Browsable(false)]
        public string Name;

        [Browsable(true)]
        public string Namespace { get; set; }

        [Browsable(false)]
        public int X, Y;

        [Browsable(true)]
        public TileCollision Collision { get; set; }

        [Browsable(true)]
        public ZPosition ZPosition { get; set; }

        [Browsable(true)]
        public List<Extra> Extras { get; set; }

        public Block(string name, string ns, Bitmap image)
        {
            Name = name;
            Namespace = ns;
            Image = image;
            Collision = TileCollision.Passable;
            ZPosition = ZPosition.OnLevel;
        }

        public Block(string name, string ns, Bitmap image, int x, int y, TileCollision collision, ZPosition zpos)
        {
            Name = name;
            Namespace = ns;
            Image = image;

            X = x;
            Y = y;

            Collision = collision;
            ZPosition = zpos;
        }

        public Block(Block block, int x, int y)
        {
            Name = block.Name;
            Namespace = block.Namespace;
            Image = block.Image;
            Collision = block.Collision;
            ZPosition = block.ZPosition;

            X = x;
            Y = y;

            Extras = block.Extras ?? new List<Extra>();

        }

        public override string ToString()
        {
            return Name;
        }

        #region Defaults

        //Sprites
        public static Block Player;
        public static Block Exit;

        public static Block CoinGold;
        public static Block CoinSilver;
        public static Block CoinBronze;

        public static Block Slime;
        public static Block Fly;

        public static Block BlueLock, RedLock, YellowLock, GreenLock;
        public static Block BlueKey, RedKey, YellowKey, GreenKey;

        public static Block Plank, Bridge;
        public static Block Fence, BrokenFence;

        public static Block Grass, Shroom, Bush, AlienPlant, Rock;

        public static Block Lava, Water, WaterUnder;

        public static Block Spikes;

        static Block()
        {
            try
            {
                Water = new Block("water", "water", FromFile("water"));
                WaterUnder = new Block("water_under", "water", FromFile("water_under"));

                Lava = new Block("lava", "block", FromFile("lava"))
                {
                    Collision = TileCollision.Killable
                };


                Spikes = new Block("spikes", "block", FromFile("spikes"))
                {
                    Collision = TileCollision.Killable
                };


                Plank = new Block("plank", "block", FromFile("plank"))
                {
                    Collision = TileCollision.Platform
                };

                Bridge = new Block("bridge", "block", FromFile("bridge"))
                {
                    Collision = TileCollision.Platform
                };


                Grass = new Block("grass", "block", FromFile("grass"))
                {
                    Collision = TileCollision.Passable,
                    ZPosition = ZPosition.Behind
                };

                Bush = new Block("bush", "block", FromFile("bush"))
                {
                    Collision = TileCollision.Passable,
                    ZPosition = ZPosition.Behind
                };

                Shroom = new Block("shroom", "block", FromFile("shroom"))
                {
                    Collision = TileCollision.Passable,
                    ZPosition = ZPosition.Behind
                };

                AlienPlant = new Block("alien_plant", "block", FromFile("alien_plant"))
                {
                    Collision = TileCollision.Passable,
                    ZPosition = ZPosition.Behind
                };
                Rock = new Block("rock", "block", FromFile("rock"))
                {
                    Collision = TileCollision.Passable,
                    ZPosition = ZPosition.Behind
                };


                Fence = new Block("fence", "block", FromFile("fence"))
                {
                    Collision = TileCollision.Passable,
                    ZPosition = ZPosition.Behind
                };

                BrokenFence = new Block("fence_broken", "block", FromFile("fence_broken"))
                {
                    Collision = TileCollision.Passable,
                    ZPosition = ZPosition.Behind
                };

                Player = new Block("player", "player", FromFile("player"));
                Exit = new Block("exit", "exit", FromFile("exit"));

                CoinGold = new Block("coin_gold", "gem", FromFile("coin_gold"));
                CoinSilver = new Block("coin_silver", "gem", FromFile("coin_silver"));
                CoinBronze = new Block("coin_bronze", "gem", FromFile("coin_bronze"));

                Slime = new Block("slime", "slime", FromFile("slime"));
                Fly = new Block("fly", "fly", FromFile("fly"));



                DefaultBlocks = new[]
            {
                Plank,
                Bridge,

                Player,
                Exit,

                CoinBronze,
                CoinGold,
                CoinSilver,

                Fly,
                Slime,

                RedKey,
                BlueKey,
                GreenKey,
                YellowKey,

                RedLock,
                BlueLock,
                GreenLock,
                YellowLock,

                Fence,
                BrokenFence,

                Grass,
                Bush,
                Shroom,
                AlienPlant,
                Rock,

                Water,
                WaterUnder,
                Lava,

                Spikes
            };
            }
            catch { }
        }

        public static Bitmap FromFile(string file)
        {
            var bitmap = System.Drawing.Image.FromFile(file.Contains(".png") ? file : "Images/" + file + ".png") as Bitmap;

            if (bitmap == null)
                return null;

            Bitmap mappy = new Bitmap(bitmap);
            using (var graphics = Graphics.FromImage(mappy))
            {
                graphics.Clear(Color.White);
                graphics.DrawImage(bitmap, 0, 0);
            }
            return mappy;
        }

        public static Block[] DefaultBlocks;


        #endregion

    }
}

