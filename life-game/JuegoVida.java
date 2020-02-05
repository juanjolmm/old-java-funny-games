import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
public class JuegoVida extends JFrame implements KeyListener
{
	private int FILAS=30;
	private int COLUMNAS=30;
	private Celda celulas[][]=new Celda[FILAS][COLUMNAS];
	private JPanel panel=new JPanel(new GridLayout(FILAS,COLUMNAS));
	public JuegoVida()
	{
		super("El juego de la vida");
		this.addKeyListener(this);
		this.addWindowListener(new WindowAdapter(){public void windowClosing(WindowEvent ev){System.exit(0);}});
		this.setBackground(Color.black);
		this.setSize(580,580);
		this.setResizable(false);
		this.initVent();
		this.getContentPane().add(panel);
	}
	public void initVent()
	{
		for(int i=0;i<FILAS;i++)
		{
			for(int j=0;j<COLUMNAS;j++)
			{
				celulas[i][j]=new Celda();
				panel.add(celulas[i][j]);
			}
		}
	}
	public void compruebaVecinos(int i, int j)
	{
		int contador=0,superior,debajo,izquierda,derecha;
		superior=i-1;
		if(superior<0)
		{
			superior=FILAS-1;
		}
		debajo=i+1;
		if(debajo>FILAS-1)
		{
			debajo=0;
		}
		izquierda=j-1;
		if(izquierda<0)
		{
			izquierda=COLUMNAS-1;
		}
		derecha=j+1;
		if(derecha>COLUMNAS-1)
		{
			derecha=0;
		}
		contador+=celulas[superior][izquierda].getCelula();
		contador+=celulas[superior][j].getCelula();
		contador+=celulas[superior][derecha].getCelula();
		contador+=celulas[i][izquierda].getCelula();
		contador+=celulas[i][derecha].getCelula();
		contador+=celulas[debajo][izquierda].getCelula();
		contador+=celulas[debajo][j].getCelula();
		contador+=celulas[debajo][derecha].getCelula();
		if(celulas[i][j].getCelula()==1 && contador>3 || contador<2)
		{
			celulas[i][j].setEstado(0);
		}
		else if(celulas[i][j].getCelula()==0 && contador==3)
		{
			celulas[i][j].setEstado(1);
		}
	}
	public void nextGen()
	{
		for(int i=0;i<FILAS;i++)
		{
			for(int j=0;j<COLUMNAS;j++)
			{
				compruebaVecinos(i,j);
			}
		}
		for(int i=0;i<FILAS;i++)
		{
			for(int j=0;j<COLUMNAS;j++)
			{
				celulas[i][j].repaint();
			}
		}
	}
	public void keyPressed(KeyEvent e)
	{
		if(e.getKeyCode()==10)
		{
			this.borraPantalla();
		}
		else
		{
			this.nextGen();
		}
	}
	public void borraPantalla()
	{
		for(int i=0;i<FILAS;i++)
		{
			for(int j=0;j<COLUMNAS;j++)
			{
				celulas[i][j].setEstado(0);
				celulas[i][j].repaint();
			}
		}
	}
	public void keyReleased(KeyEvent e){}
	public void keyTyped(KeyEvent e){}
	public static void main(String []args)
	{
		JuegoVida ventana=new JuegoVida();
		ventana.show();
	}
}
class Celda extends JPanel implements MouseListener
{
	private int esCelula,estadoNuevo;
	public Celda()
	{
		super();
		esCelula=0;
		estadoNuevo=0;
		//this.setBorder(BorderFactory.createLineBorder(Color.white));
		this.addMouseListener(this);
		this.setBackground(Color.black);
	}
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		g.setColor(Color.green);
		if(estadoNuevo==1)
		{
			g.fillOval(1,1,15,15);
			esCelula=1;
		}
		else
		{
			esCelula=0;
		}
	}
	public int getCelula()
	{
		return esCelula;	
	}
	public void setEstado(int valor)
	{
		estadoNuevo=valor;
	}
	public void mouseEntered(MouseEvent e){}
	public void mouseExited(MouseEvent e){}
	public void mouseReleased(MouseEvent e){}
	public void mousePressed(MouseEvent e){}
	public void mouseClicked(MouseEvent e)
	{
		if(esCelula==0)
		{
			estadoNuevo=1;
		}
		else
		{
			estadoNuevo=0;	
		}
		repaint();
	}

}