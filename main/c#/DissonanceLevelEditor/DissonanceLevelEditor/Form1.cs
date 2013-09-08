using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Diagnostics;
using System.Drawing;
using System.Globalization;
using System.IO;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace PewPewLevelEditor
{
    public partial class Form1 : Form
    {
        private readonly Level _level;

        private readonly List<List<Block>> _blocks;

        private readonly List<Block> _selectedBlocks;

        private string _selectedBlockToPaint;

        private List<Block[]> _currentHistory, _blocksUndid;

        public bool IsDeleteDown { get; set; }
        public bool IsPlaceDown { get; set; }

        public Form1()
        {
            InitializeComponent();
            _level = new Level();
            _blocks = new List<List<Block>>();
            _selectedBlocks = new List<Block>();
        }

        private void mCountWidth_ValueChanged(object sender, EventArgs e)
        {
            if ((int)mCountWidth.Value > 0)
                _level.Width = (int)mCountWidth.Value;
            updateSize();
        }

        private void mCountHeight_ValueChanged(object sender, EventArgs e)
        {
            if ((int)mCountHeight.Value > 0)
                _level.Height = (int)mCountHeight.Value;
            updateSize();
        }

        private const int CellSize = 40;

        private void updateSize()
        {
            int width = _level.Width;
            int height = _level.Height;

            if (height == 0 || width == 0)
                return;

            mCountHeight.Value = height;
            mCountWidth.Value = width;

            while (mGrid.Columns.Count != width)
            {
                if (mGrid.Columns.Count > width)
                {
                    mGrid.Columns.RemoveAt(mGrid.Columns.Count - 1);
                    _blocks.RemoveAt(_blocks.Count - 1);
                }
                else
                {
                    var header = (mGrid.Columns.Count + 1).ToString(CultureInfo.InvariantCulture);
                    mGrid.Columns.Add(header, header);
                    var list = new List<Block>();
                    while (list.Count < mGrid.Rows.Count)
                        list.Add(null);
                    _blocks.Add(list);
                    var dataGridViewColumn = mGrid.Columns[header];
                    if (dataGridViewColumn != null) dataGridViewColumn.Width = CellSize;
                }
            }

            while (mGrid.Rows.Count != height)
            {
                if (mGrid.Rows.Count > height)
                {
                    mGrid.Rows.RemoveAt(mGrid.Rows.Count - 1);
                    foreach (var list in _blocks)
                    {
                        list.RemoveAt(list.Count - 1);
                    }
                }
                else
                {
                    mGrid.Rows.Add(new DataGridViewRow());
                    foreach (var list in _blocks)
                    {
                        while (list.Count < mGrid.Rows.Count)
                            list.Add(null);
                    }
                    var dataGridViewRow = mGrid.Rows[mGrid.Rows.Count - 1];
                    dataGridViewRow.Height = CellSize;
                }

            }


        }

        // Add/Delete blocks
        private void mGrid_CellMouseClick(object sender, DataGridViewCellMouseEventArgs e)
        {
            if (mGrid.SelectedCells.Count <= 0) return;

            if (e.Button == System.Windows.Forms.MouseButtons.Right)
            {
                foreach (DataGridViewCell cell in mGrid.SelectedCells)
                {
                    int y = cell.RowIndex;
                    int x = cell.ColumnIndex;

                    Block block = _blocks[x][y];

                    if (IsDeleteDown)
                    {
                        if (block == null)
                        {
                            continue;
                        }
                        mGrid[x, y] = new DataGridViewLinkCell();
                        _blocks[x][y] = null;
                        mProps.SelectedObject = null;

                        _currentHistory.Add(new[] { block, null });

                    }

                    else if (IsPlaceDown)
                    {
                        if (block != null || _selectedBlockToPaint == null)
                        {
                            continue;
                        }

                        Block baseBlock = null;
                        foreach (var based in Block.DefaultBlocks.Where(based => based.Name == _selectedBlockToPaint))
                        {
                            baseBlock = based;
                        }

                        if (baseBlock == null)
                        {
                            baseBlock = new Block(_selectedBlockToPaint, "block", Block.FromFile(_selectedBlockToPaint));
                        }

                        block = new Block(baseBlock, x, y);

                        mProps.SelectedObject = block;

                        DataGridViewImageCell imgCell = new DataGridViewImageCell
                        {
                            ValueType = typeof(Bitmap),
                            Value = block.Image,
                            ImageLayout = DataGridViewImageCellLayout.Stretch,
                            Style = { BackColor = Color.White }
                        };
                        mGrid[x, y] = new DataGridViewLinkCell();
                        mGrid.Update();
                        mGrid[x, y] = imgCell;
                        _blocks[x][y] = block;

                        _currentHistory.Add(new[] { null, block });

                    }

                }

            }
            else if (mGrid.SelectedCells.Count <= 1)
            {

                var cell = mGrid.SelectedCells[0];

                int y = cell.RowIndex;
                int x = cell.ColumnIndex;

                Block block = _blocks[x][y];

                if (IsDeleteDown)
                {
                    if (block == null)
                    {
                        return;
                    }
                    mGrid[x, y] = new DataGridViewLinkCell();
                    _blocks[x][y] = null;
                    mProps.SelectedObject = null;

                    _currentHistory.Add(new[] { block, null });

                }
                else if (IsPlaceDown)
                {
                    if (block != null || _selectedBlockToPaint == null)
                    {
                        return;
                    }

                    Block baseBlock = null;
                    foreach (var based in Block.DefaultBlocks.Where(based => based.Name == _selectedBlockToPaint))
                    {
                        baseBlock = based;
                    }

                    if (baseBlock == null)
                    {
                        baseBlock = new Block(_selectedBlockToPaint, "block", Block.FromFile(_selectedBlockToPaint));
                    }

                    block = new Block(baseBlock, x, y);

                    mProps.SelectedObject = block;

                    DataGridViewImageCell imgCell = new DataGridViewImageCell
                    {
                        ValueType = typeof(Bitmap),
                        Value = block.Image,
                        ImageLayout = DataGridViewImageCellLayout.Stretch,
                        Style = { BackColor = Color.White }
                    };
                    mGrid[x, y] = new DataGridViewLinkCell();
                    mGrid.Update();
                    mGrid[x, y] = imgCell;
                    _blocks[x][y] = block;

                    _currentHistory.Add(new[] { null, block });

                }
                else
                {
                    _selectedBlocks.Clear();

                    if (block != null)
                    {
                        _selectedBlocks.Add(block);
                    }
                    
                    mProps.SelectedObjects = _selectedBlocks.ToArray();
                }

            }
            else
            {
                _selectedBlocks.Clear();
                foreach (DataGridViewCell cell in mGrid.SelectedCells)
                {
                    int y = cell.RowIndex;
                    int x = cell.ColumnIndex;

                    Block block = _blocks[x][y];

                    if (block != null)
                    {
                        _selectedBlocks.Add(block);
                        
                    }
                }
                mProps.SelectedObjects = _selectedBlocks.ToArray();

            }
        }


        private void Form1_Load(object sender, EventArgs e)
        {
            updateSize();

            _currentHistory = new List<Block[]>();
            _blocksUndid = new List<Block[]>();


            if (!Directory.Exists("Images"))
            {
                Directory.CreateDirectory("Images");
                return;
            }
            foreach (var image in Directory.EnumerateFiles("Images"))
            {
                AddToList(image, (o, args) =>
                {
                    _selectedBlockToPaint = ((RadioButton)o).Tag.ToString().Replace("Images\\", "").Replace("Images/", "").Replace(".png", "");
                });
            }
        }

        private void AddToList(string image, EventHandler onClick)
        {
            Bitmap map = (Bitmap)Image.FromFile(image);
            Bitmap resized = new Bitmap(map, 35, 35);

            RadioButton button = new RadioButton
            {
                Appearance = Appearance.Button,
                Image = resized,
                Width = 39,
                Height = 39
            };
            button.Click += onClick;
            button.Tag = image;
            pnlBlocks.Controls.Add(button);

        }


        private void mProps_PropertyValueChanged(object s, PropertyValueChangedEventArgs e)
        {
            Block block = mProps.SelectedObject as Block;

            if (block == null)
                return;

            int x = block.X;
            int y = block.Y;

            DataGridViewImageCell imgCell = new DataGridViewImageCell
            {
                ValueType = typeof(Bitmap),
                Value = block.Image,
                ImageLayout = DataGridViewImageCellLayout.Stretch,
                Style = { BackColor = Color.White }
            };
            mGrid[x, y] = new DataGridViewLinkCell();
            mGrid.Update();
            mGrid[x, y] = imgCell;
        }

        private List<Block> compileBlocks()
        {
            int width = (int)mCountWidth.Value;
            int height = (int)mCountHeight.Value;

            List<Block> blocks = new List<Block>(width * height);
            blocks.AddRange(from list in _blocks from block in list where block != null select block);

            return blocks;
        }

        private void openToolStripMenuItem_Click(object sender, EventArgs e)
        {
            OpenFileDialog dialog = new OpenFileDialog
            {
                Filter = "Xml|*.xml",
                Title = "Open Level"
            };

            dialog.ShowDialog(this);

            string file = dialog.FileName;

            if (!file.EndsWith(".xml")) return;

            MessageBox.Show(_level.Load(file) ? "Level Loaded!" : "Error loading level");

            _currentHistory = new List<Block[]>();
            _blocksUndid = new List<Block[]>();

            loadLevel();

            this.Text = "PewPew Level Editor - " + Path.GetFileName(file);
        }

        private void loadLevel()
        {
            updateSize();
            mCountHeight.Value = _level.Height;
            mCountWidth.Value = _level.Width;



            for (int x = 0; x < mGrid.Columns.Count; x++)
            {
                for (int y = 0; y < mGrid.Rows.Count; y++)
                {
                    mGrid[x, y] = new DataGridViewLinkCell();
                    _blocks[x][y] = null;
                }
            }


            refresh();

        }

        private void refresh()
        {
            foreach (var block in _level.Blocks)
            {
                _blocks[block.X][block.Y] = block;
                DataGridViewImageCell imgCell = new DataGridViewImageCell
                {
                    ValueType = typeof(Bitmap),
                    Value = block.Image,
                    ImageLayout = DataGridViewImageCellLayout.Stretch,
                    Style = { BackColor = Color.White }

                };
                mGrid[block.X, block.Y] = new DataGridViewLinkCell();
                mGrid.Update();
                mGrid[block.X, block.Y] = imgCell;
            }
        }

        private void Form1_FormClosing(object sender, FormClosingEventArgs e)
        {
            switch (MessageBox.Show("Would you like to save the map?", "Save map?", MessageBoxButtons.YesNoCancel))
            {
                case DialogResult.Cancel:
                    e.Cancel = true;
                    return;
                case DialogResult.Yes:
                    saveToolStripMenuItem_Click(null, null);
                    return;
            }
        }

        private void newToolStripMenuItem_Click(object sender, EventArgs e)
        {
            switch (MessageBox.Show("Would you like to save the map?", "Save map?", MessageBoxButtons.YesNoCancel))
            {
                case DialogResult.Cancel:
                    return;
                case DialogResult.Yes:
                    saveToolStripMenuItem_Click(null, null);
                    break;
            }

            for (int x = 0; x < mGrid.Columns.Count; x++)
            {
                for (int y = 0; y < mGrid.Rows.Count; y++)
                {
                    mGrid[x, y] = new DataGridViewLinkCell();

                    _blocks[x][y] = null;
                }
            }

            _currentHistory = new List<Block[]>();
            _blocksUndid = new List<Block[]>();
        }

        private void testToolStripMenuItem_Click(object sender, EventArgs e)
        {
            _level.Blocks = compileBlocks();
            _level.Save("temp.xml");

            string request = Path.GetFullPath("temp.xml");

            try
            {
                Process.Start("PewPew.exe", request);
            }
            catch { MessageBox.Show("Cannot find PewPew game. Try reinstalling the game setup"); }
        }


        private void contentsToolStripMenuItem_Click(object sender, EventArgs e)
        {
            var sb = new StringBuilder();

            sb.AppendLine("How to use:");
            sb.AppendLine("1: Select block from the left panel");
            sb.AppendLine("2: Double left click in canvas area to add block");
            sb.AppendLine("3: To erase, double right click a block");
            sb.AppendLine();
            sb.AppendLine("Workflow:");
            sb.AppendLine("Properties - right panel, contorls properties of selected block");
            sb.AppendLine("Canvas - center panel, paint/remove blocks for level");
            sb.AppendLine("Blocks - left panel, select from all blocks that can be used ingame");
            sb.AppendLine("  NOTE: Not all blocks can be used. Yet...");
            sb.AppendLine();
            sb.AppendLine("Testing:");
            sb.AppendLine("  NOTE: you must have PewPew already installed.");
            sb.AppendLine("Click \"Test\" in the menu.");
            sb.AppendLine("Make sure you have a player and an exit or it will crash");
            sb.AppendLine("If it crashes for reasons not above, report back to @headdetect k? k.");
            sb.AppendLine();
            sb.AppendLine("Finishing:");
            sb.AppendLine("File > Save. Not that hard doe.");

            MessageBox.Show(this, sb.ToString(), "Help - PewPew Level Creator", MessageBoxButtons.OK, MessageBoxIcon.Question);
        }

        void notYet()
        {
            MessageBox.Show("This feature is not ready yet... :(");
        }

        private void saveToolStripMenuItem_Click(object sender, EventArgs e)
        {
            int width = (int)mCountWidth.Value;
            int height = (int)mCountHeight.Value;

            if (height == 0 || width == 0)
                return;

            SaveFileDialog dialog = new SaveFileDialog
            {
                Filter = "XML|*.xml",
                Title = "Save level"
            };

            dialog.ShowDialog(this);

            string file = dialog.FileName;

            if (!file.EndsWith(".xml")) return;

            _level.Blocks = compileBlocks();
            _level.Save(file);

            MessageBox.Show(this, "Level Saved!");
        }

        private void exitToolStripMenuItem_Click(object sender, EventArgs e)
        {
            Close();
        }

        private void undoToolStripMenuItem1_Click(object sender, EventArgs e)
        {
            if (_currentHistory.Count <= 0)
                return;

            Block[] blockPlaced = _currentHistory[_currentHistory.Count - 1];

            Block from = blockPlaced[0];
            Block to = blockPlaced[1];

            // Change back to air //
            if (from == null)
            {
                if (to != null)
                {
                    mGrid[to.X, to.Y] = new DataGridViewLinkCell();
                    _blocks[to.X][to.Y] = null;
                    mProps.SelectedObject = null;
                }
            }
            else
            {
                // Change from current block to previous block
                if (to != null)
                {
                    DataGridViewImageCell imgCell = new DataGridViewImageCell
                    {
                        ValueType = typeof(Bitmap),
                        Value = from.Image,
                        ImageLayout = DataGridViewImageCellLayout.Stretch,
                        Style = { BackColor = Color.White }
                    };
                    mGrid[to.X, to.Y] = new DataGridViewLinkCell();
                    mGrid.Update();
                    mGrid[to.X, to.Y] = imgCell;
                    mProps.SelectedObject = from;
                    _blocks[to.X][to.Y] = from;
                }
                else
                {
                    mGrid[from.X, from.Y] = new DataGridViewLinkCell();
                    _blocks[from.X][from.Y] = null;
                    mProps.SelectedObject = null;
                }
            }


            _currentHistory.Remove(blockPlaced);
            _blocksUndid.Add(blockPlaced);
        }

        private void redoToolStripMenuItem1_Click(object sender, EventArgs e)
        {
            if (_blocksUndid.Count <= 0)
                return;

            Block[] blockPlaced = _blocksUndid[_blocksUndid.Count - 1];

            Block from = blockPlaced[0];
            Block to = blockPlaced[1];

            // Change back to air //
            if (from != null)
            {
                if (to == null)
                {
                    mGrid[from.X, from.Y] = new DataGridViewLinkCell();
                    _blocks[from.X][from.Y] = null;
                    mProps.SelectedObject = null;
                }
                else
                {
                    mGrid[from.X, from.Y] = new DataGridViewLinkCell();
                    _blocks[from.X][from.Y] = null;
                    mProps.SelectedObject = null;
                }
            }
            else
            {
                // Change from current block to previous block
                if (to != null)
                {
                    DataGridViewImageCell imgCell = new DataGridViewImageCell
                    {
                        ValueType = typeof(Bitmap),
                        Value = to.Image,
                        ImageLayout = DataGridViewImageCellLayout.Stretch,
                        Style = { BackColor = Color.White }
                    };
                    mGrid[to.X, to.Y] = new DataGridViewLinkCell();
                    mGrid.Update();
                    mGrid[to.X, to.Y] = imgCell;
                    mProps.SelectedObject = to;
                    _blocks[to.X][to.Y] = to;
                }

            }


            _blocksUndid.Remove(blockPlaced);
            _currentHistory.Add(blockPlaced);
        }

        private void customizeToolStripMenuItem_Click(object sender, EventArgs e)
        {
            notYet();
        }

        private void optionsToolStripMenuItem_Click(object sender, EventArgs e)
        {
            notYet();
        }

        private void aboutToolStripMenuItem_Click(object sender, EventArgs e)
        {
            notYet();
        }

        private void Form1_KeyDown(object sender, KeyEventArgs e)
        {
            if (e.KeyCode == Keys.Q)
                IsPlaceDown = true;
            if (e.KeyCode == Keys.W)
                IsDeleteDown = true;

        }

        private void Form1_KeyUp(object sender, KeyEventArgs e)
        {
            if (e.KeyCode == Keys.Q)
                IsPlaceDown = false;
            if (e.KeyCode == Keys.W)
                IsDeleteDown = false;
        }

        private void levelPropertiesToolStripMenuItem_Click(object sender, EventArgs e)
        {
            mProps.SelectedObject = _level;
        }


    }
}
