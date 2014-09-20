/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2014 Bjoern Poetzschke<bjoern.poetzschke@gmail.com>
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.border.TitledBorder;

public class YAJFontChooser extends JDialog
{
	
	public static Font showDialog()
	{
		return showDialog(null);
	}
	
	
	public static Font showDialog(Font _PreselectedFont)
	{
		YAJFontChooser fontChooser = new YAJFontChooser(_PreselectedFont);
		fontChooser.setVisible(true);
		
		return fontChooser.getSelectedFont();
	}
	
	private static final long	serialVersionUID	= -3162469862533723830L;
	
	private JPanel						m_ButtonPanel			= null;
	private JButton 					m_OkButton				= null;
	private JButton 					m_CancelButton			= null;
	private JPanel 						m_FontSelectionPanel	= null;
	private Font						m_SelectedFont			= null;
	private JLabel						m_FontLabel				= null;
	private JTextField					m_FontFamilyTextField	= null;
	private JScrollPane					m_FontFamilyScrollPane	= null;
	private JList<String>				m_FontFamilyList		= null;
	private HashMap<String, List<Font>>	m_FontMap				= null;
	private JLabel						m_FontStyleLabel		= null;
	private JTextField					m_FontStyleTextField	= null;
	private JScrollPane 				m_FontStyleScrollPane	= null;
	private JList<FontStyleSelection>	m_FontStyleList			= null;
	private JPanel 						m_PreviewPanel			= null;
	private JLabel 						m_PreviewLabel			= null;
	private JLabel						m_FontSizeLabel			= null;
	private JTextField					m_FontSizeTextField		= null;
	private JScrollPane					m_FontSizeScrollPane	= null;
	private JList<Integer>				m_FontSizeList			= null;
	private ButtonActionListener		m_ActionListener		= new ButtonActionListener();
	private SelectionListener			m_SelectionListener		= new SelectionListener();
	
	private static int[] 				DEFAULT_FONT_SIZES		= {5,6,7,8,9,10,11,12,13,14,18,24,36,48,64,72,96};
	
	private YAJFontChooser(Font _PreselectedFont)
	{
		super();
		
		initUI();
		
		//required to wait for close of dialog
		setModal(true);
		
		initFonts();
		initFontSizeList();
		
		if(_PreselectedFont != null)
		{
			
		}
	}
	
	private void initUI()
	{
		setSize(500,400);
		setPreferredSize(getSize());
		setResizable(false);
		setLocationRelativeTo(null);
		
		setTitle("Select Font");
		
		m_ButtonPanel = new JPanel();
		getContentPane().add(m_ButtonPanel, BorderLayout.SOUTH);
		
		m_OkButton = new JButton("OK");
		m_OkButton.addActionListener(m_ActionListener);
		m_ButtonPanel.add(m_OkButton);
		
		m_CancelButton = new JButton("Cancel");
		m_ButtonPanel.add(m_CancelButton);
		m_CancelButton.addActionListener(m_ActionListener);
		
		m_FontSelectionPanel = new JPanel();
		getContentPane().add(m_FontSelectionPanel, BorderLayout.CENTER);
		m_FontSelectionPanel.setLayout(null);
		
		m_FontLabel = new JLabel("Font");
		m_FontLabel.setBounds(6, 6, 61, 16);
		m_FontSelectionPanel.add(m_FontLabel);
		
		m_FontFamilyTextField = new JTextField();
		m_FontFamilyTextField.setEditable(false);
		m_FontFamilyTextField.setHorizontalAlignment(SwingConstants.LEFT);
		m_FontFamilyTextField.setEnabled(false);
		m_FontFamilyTextField.setBounds(2, 24, 193, 20);
		m_FontSelectionPanel.add(m_FontFamilyTextField);
		m_FontFamilyTextField.setColumns(10);
		
		m_FontFamilyScrollPane = new JScrollPane();
		m_FontFamilyScrollPane.setBorder(null);
		m_FontFamilyScrollPane.setBounds(6, 51, 185, 175);
		m_FontSelectionPanel.add(m_FontFamilyScrollPane);
		
		m_FontFamilyList = new JList<String>();
		m_FontFamilyList.setBorder(null);
		m_FontFamilyList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		m_FontFamilyList.addListSelectionListener(m_SelectionListener);
		m_FontFamilyScrollPane.setViewportView(m_FontFamilyList);
		
		m_FontStyleLabel = new JLabel("Style");
		m_FontStyleLabel.setBounds(200, 6, 61, 16);
		m_FontSelectionPanel.add(m_FontStyleLabel);
		
		m_FontStyleTextField = new JTextField();
		m_FontStyleTextField.setEnabled(false);
		m_FontStyleTextField.setEditable(false);
		m_FontStyleTextField.setBounds(196, 24, 148, 20);
		m_FontSelectionPanel.add(m_FontStyleTextField);
		m_FontStyleTextField.setColumns(10);
		
		m_FontStyleScrollPane = new JScrollPane();
		m_FontStyleScrollPane.setBounds(200, 51, 140, 175);
		m_FontStyleScrollPane.setBorder(null);
		m_FontSelectionPanel.add(m_FontStyleScrollPane);
		
		m_FontStyleList = new JList<FontStyleSelection>();
		m_FontStyleList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		m_FontStyleList.setBorder(null);
		m_FontStyleList.addListSelectionListener(m_SelectionListener);
		m_FontStyleScrollPane.setViewportView(m_FontStyleList);
		
		m_FontSizeLabel = new JLabel("Size");
		m_FontSizeLabel.setBounds(349, 6, 61, 16);
		m_FontSelectionPanel.add(m_FontSizeLabel);
		
		m_FontSizeTextField = new JTextField();
		m_FontSizeTextField.setBounds(345, 24, 138, 20);
		m_FontSelectionPanel.add(m_FontSizeTextField);
		m_FontSizeTextField.setColumns(10);
		
		m_PreviewPanel = new JPanel();
		m_PreviewPanel.setBorder(new TitledBorder(null, "Preview", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		m_PreviewPanel.setBounds(6, 235, 488, 98);
		m_FontSelectionPanel.add(m_PreviewPanel);
		m_PreviewPanel.setLayout(new BorderLayout(0, 0));
		
		m_PreviewLabel = new JLabel("");
		m_PreviewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		m_PreviewPanel.add(m_PreviewLabel);
		
		m_FontSizeScrollPane = new JScrollPane();
		m_FontSizeScrollPane.setBounds(349, 51, 130, 175);
		m_FontSelectionPanel.add(m_FontSizeScrollPane);
		
		m_FontSizeList = new JList<Integer>();
		m_FontSizeList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		m_FontSizeList.setBorder(null);
		m_FontSizeList.addListSelectionListener(m_SelectionListener);
		m_FontSizeScrollPane.setViewportView(m_FontSizeList);
	}
	
	private void initFonts()
	{
		m_FontMap = new HashMap<String, List<Font>>();
		
		GraphicsEnvironment graphicsEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment();
		
		Font[] fonts = graphicsEnvironment.getAllFonts();
		
		for(Font font : fonts)
		{
			//check if hasmap entry exists
			String fontFamily = font.getFamily();
			
			List<Font> fontFamilyList = m_FontMap.get(fontFamily);
			
			//create new list if there is no one for the font family
			if(fontFamilyList == null)
			{
				fontFamilyList = new ArrayList<Font>();
				m_FontMap.put(fontFamily, fontFamilyList);
			}
			
			fontFamilyList.add(font);
		}
		
		String[] fontFamilyNames = graphicsEnvironment.getAvailableFontFamilyNames();
		
		DefaultListModel<String> fontFamilyListModel = new DefaultListModel<String>();
		for(String fontFamily : fontFamilyNames)
		{
			fontFamilyListModel.addElement(fontFamily);
		}
		
		m_FontFamilyList.setModel(fontFamilyListModel);
		
		m_FontFamilyList.setSelectedIndex(0);
	}
	
	private void initFontSizeList()
	{
		DefaultListModel<Integer> sizeListModel = new DefaultListModel<Integer>();
		
		for(int sizeIndex = 0; sizeIndex < DEFAULT_FONT_SIZES.length; sizeIndex++)
		{
			sizeListModel.addElement(new Integer(DEFAULT_FONT_SIZES[sizeIndex]));
		}
		
		m_FontSizeList.setModel(sizeListModel);
		m_FontSizeList.setSelectedIndex(7);
	}
	
	private void loadFontsForFamily(String _FontFamily)
	{
		DefaultListModel<FontStyleSelection> familyListModel = new DefaultListModel<FontStyleSelection>();
		
		List<Font> familyFonts = m_FontMap.get(_FontFamily);
		
		for(Font font : familyFonts)
		{
			familyListModel.addElement(new FontStyleSelection(font));
		}
		
		m_FontStyleList.setModel(familyListModel);
		m_FontStyleList.setSelectedIndex(0);
	}
	
	private Font getSelectedFont()
	{
		return m_SelectedFont;
	}
	
	//----------------------------------------------------------------------------
	// Internal classes
	//----------------------------------------------------------------------------
	
	private class SelectionListener implements ListSelectionListener
	{
		@Override
		public void valueChanged(ListSelectionEvent _Event)
		{
			if(_Event.getSource().equals(m_FontFamilyList))
			{
				m_FontFamilyTextField.setText(m_FontFamilyList.getSelectedValue());
				m_FontFamilyTextField.setCaretPosition(0);
				
				loadFontsForFamily(m_FontFamilyTextField.getText());
			}
			else if(_Event.getSource().equals(m_FontStyleList))
			{
				FontStyleSelection styleSelection = m_FontStyleList.getSelectedValue();
				if(styleSelection != null)
				{
					m_FontStyleTextField.setText(styleSelection.toString());
					m_FontStyleTextField.setCaretPosition(0);
				
					m_PreviewLabel.setText(styleSelection.toString());
					Font font = new Font(styleSelection.getSelection().getFontName(), styleSelection.getSelection().getStyle(), 10);
					m_PreviewLabel.setFont(font);
					
					m_SelectedFont = font;
				}
			}
			else if(_Event.getSource().equals(m_FontSizeList))
			{
				FontStyleSelection styleSelection = m_FontStyleList.getSelectedValue();
				if(styleSelection != null)
				{
					m_FontSizeTextField.setText(m_FontSizeList.getSelectedValue().toString());
					m_FontSizeTextField.setCaretPosition(m_FontSizeTextField.getText().length());
					
					Font font = new Font(styleSelection.getSelection().getFontName(), styleSelection.getSelection().getStyle(), m_FontSizeList.getSelectedValue().intValue());
					
					m_PreviewLabel.setFont(font);
					
					m_SelectedFont = font;
				}
			}
		}
	}
	
	private class ButtonActionListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent _Event)
		{
			if(_Event.getSource().equals(m_CancelButton))
			{
				setVisible(false);
				m_SelectedFont = null;
			}
			else if(_Event.getSource().equals(m_OkButton))
			{
				setVisible(false);
			}
		}
	}
	
	private class FontStyleSelection
	{
		public FontStyleSelection(Font _Font)
		{
			m_Selection = _Font;
		}
		
		@Override
		public String toString()
		{
			String[] fontNameParts = m_Selection.getFontName().split("-");
			if(fontNameParts.length == 2)
			{
				//see http://stackoverflow.com/a/17512351 for splitting string at uppercase
				String[] parts = fontNameParts[1].split("(?<=\\p{Ll})(?=\\p{Lu})");
				String fontText = "";
				
				for(int partIndex = 0; partIndex < parts.length; partIndex++)
				{
					fontText += parts[partIndex];
					
					if(partIndex < (parts.length - 1))
					{
						fontText +=" ";
					}
				}
				
				return fontText;
			}
			else
			{
				return "Regular";
			}
		}
		
		public Font getSelection()
		{
			return m_Selection;
		}
		
		private Font m_Selection = null;
	}
}