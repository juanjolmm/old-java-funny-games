import java.awt.*;
import java.awt.image.*;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.event.*;
import java.math.*;
///////////////////////////////////////////////////////////////////////
//CLASE DEL PANEL QUE PINTA Y HACE TODO
//////////////////////////////////////////////////////////////////////
class JPanelNaves extends JPanel implements Runnable
{
	private int numStars=50;
	private int numRocas=5;
	private int intervaloNave=12;
	private int duermeHilo=10;
	private int xNave=35;
	private int yNave=30;
	private int nivel=1;
	private int vidas=5;
	private int puntos=0;
	private int subeNivel=0;
	private int puntoXNave=50;
	private int estadoJuego=0;
	private int contador=0;
	//el 0 es la presentación;
	//el 1 es jugando
	//el 2 es pause
	//el 3 es game over
	//el 4 ya veremos ke es
	private boolean disparo=false;
	private boolean exploto=false;
	private Image nave;
	private Thread arranca;
	private Font fontPresent;
	private Font fontInfo;
	private Font fontNormal;
	private Estrella star[]=new Estrella[numStars];
	private Roca rocas[]=new Roca[numRocas];
	public JPanelNaves()
	{
		super();
		this.setBackground(Color.black);
		nave=Toolkit.getDefaultToolkit().getImage("nave.jpg");
		arranca=new Thread(this);
		fontPresent=new Font("Reject", Font.PLAIN, 55);
		fontInfo=new Font("Reject", Font.PLAIN, 20);
		fontNormal=new Font("Verdana", Font.PLAIN, 26);
		for(int i=0;i<numStars;i++)
		{
			star[i]=new Estrella();
		}
		for(int e=0;e<numRocas;e++)
		{
			rocas[e]=new Roca(this);
		}
		arranca.start();
	}
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		if(estadoJuego==0)
		{
			g.setFont(fontPresent);
			g.setColor(Color.red);
			g.drawString("SPACE DARKNESS",240,120);
			g.drawImage(nave,220,180,this);
			g.drawImage(Toolkit.getDefaultToolkit().getImage("roca.jpg"),650,180,this);
			g.drawImage(Toolkit.getDefaultToolkit().getImage("explosion.jpg"),420,230,this);
			g.setFont(fontNormal);
			g.drawString("PULSA ENTER PARA COMENZAR",260,410);
		}
		else if(estadoJuego==1)
		{
			g.setColor(Color.WHITE);
			for(int e=0;e<numStars;e++)
			{
				g.fillOval(star[e].getX(),star[e].getY(),star[e].getDim(),star[e].getDim());
			}
			if(!exploto)
			{
				g.drawImage(nave,xNave,yNave,this);
			}
			else
			{
				g.drawImage(Toolkit.getDefaultToolkit().getImage("explosion.jpg"),xNave,yNave,this);
			}
			if(disparo)
			{
				g.setColor(Color.RED);
				g.fillRect(xNave+90,yNave+34,1000-xNave+90,1);
				disparo=false;
			}
			for(int i=0;i<numRocas;i++)
			{
				rocas[i].dibujaRoca(g);
			}
			g.setColor(Color.RED);
			g.setFont(fontInfo);
			g.drawString("PUNTOS "+puntos,25,25);
			g.drawString("VIDAS "+vidas,800,25);
			g.drawString("NIVEL "+nivel,900,25);
			if(exploto && contador<=50)
			{
				contador++;
				if(contador==50)
				{
					exploto=false;
					contador=0;
				}
			}
		}
		else if(estadoJuego==2)
		{
			g.setColor(Color.RED);
			g.setFont(fontNormal);
			g.drawString("********** PAUSE *********",270,180);
			g.drawString("PULSA ENTER PARA CONTINUAR",270,210);
			g.drawString("**************************",270,240);
		}
		else if(estadoJuego==3)
		{
			g.setColor(Color.RED);
			g.setFont(fontNormal);
			g.drawString("********GAME OVER********",270,180);
			g.drawString("PULSA ENTER PARA COMENZAR",270,210);
			g.drawString("*************************",270,240);
			g.drawString("**PUNTOS "+puntos+"**",270,270);
		}
		else if(estadoJuego==4)
		{
			
		}
	}
	public void run()
	{
		while(true)
		{
			if(estadoJuego==1)
			{
				for(int i=0;i<numStars;i++)
				{
					star[i].moverStar();
				}
				for(int e=0;e<numRocas;e++)
				{
					rocas[e].moverRoca();
					rocas[e].controlaChoke(xNave,yNave,disparo);
				}
				try 
				{ 
					Thread.sleep(duermeHilo); 
				}
				catch (Exception e)
				{
					
				}
			}
			this.repaint();
		}
	}
	public void sumaPuntos()
	{
		puntos+=puntoXNave;
		subeNivel+=puntoXNave;
		if(subeNivel==2000)
		{
			duermeHilo-=2;
			nivel++;
			subeNivel=0;	
		}
	}
	public void naveExplosion()
	{
		vidas--;
		exploto=true;
		if(vidas==0)
		{
			estadoJuego=3;
		}
	}
	public void capturaTecleo(int code)
	{
		if(code==38)
		{
			//ARRIBA
			yNave-=intervaloNave;
		}
		else if(code==40)
		{
			//ABAJO
			yNave+=intervaloNave;
		}
		else if(code==37)
		{
			//IZQUIERDA
			xNave-=intervaloNave;
		}
		else if(code==39)
		{
			//DERECHA
			xNave+=intervaloNave;
		}
		else if(code==32)
		{
			//SPACE
			disparo=true;
		}
		else if(code==10)
		{
			//ENTER
			if(estadoJuego==0)
			{
				estadoJuego=1;
			}
			else if(estadoJuego==1)
			{
				estadoJuego=2;
			}
			else if(estadoJuego==2)
			{
				estadoJuego=1;
			}
			else if(estadoJuego==3)
			{
				vidas=5;
				puntos=0;
				estadoJuego=0;
			}
		}
	}
}
///////////////////////////////////////////////////////////////////////
//CLASE DE LA VENTANA KE SOSTIENE EL PANEL
//////////////////////////////////////////////////////////////////////
public class JNaves extends JFrame implements KeyListener
{
	private JPanelNaves panel;
	public JNaves()
	{
		super("SPACE DARKNESS");
		panel=new JPanelNaves();
		this.addKeyListener(this);
		this.getContentPane().add(panel,"Center");
		this.setSize(1000,550);
		this.setResizable(false);
		this.addWindowListener(new WindowAdapter(){public void windowClosing(WindowEvent ev){System.exit(0);}});
	}
	public void keyPressed(KeyEvent e)
	{
		int code=e.getKeyCode();
		panel.capturaTecleo(code);
	}
	public void keyReleased(KeyEvent e){}
	public void keyTyped(KeyEvent e){}
	public static void main(String []args)
	{
		JNaves ventana=new JNaves();
		ventana.show();
	}
}
///////////////////////////////////////////////////////////////////////
//CLASE DE LAS ESTRELLAS DEL ESPACIO
//////////////////////////////////////////////////////////////////////
class Estrella
{
	private int x;
	private int y;
	private int dim;
	private int INTERVALO=2;
	public Estrella()
	{
		x=(int)Math.floor(Math.random()*1000);
		y=(int)Math.floor(Math.random()*550);
		dim=(int)Math.floor(Math.random()*5);
	}
	public void moverStar()
	{
		if(x<=0)
		{
			x=1000;
			y=(int)Math.floor(Math.random()*550);
		}
		else
		{
			x-=INTERVALO;
		}
	}
	public int getX()
	{
		return x;
	}
	public int getY()
	{
		return y;
	}
	public int getDim()
	{
		return dim;
	}
}
///////////////////////////////////////////////////////////////////////
//CLASE DE LA ROCA ESPACIAL
//////////////////////////////////////////////////////////////////////
class Roca
{
	private int x;
	private int y;
	private int contador=0;
	private int INTERVALO=4;
	private boolean exploto=false;
	private Image roca;
	private Image explosion;
	private JPanelNaves panel;
	public Roca(JPanelNaves pNaves)
	{
		x=(int)Math.floor(Math.random()*1000);
		y=(int)Math.floor(Math.random()*450);
		panel=pNaves;
		roca=Toolkit.getDefaultToolkit().getImage("roca.jpg");
		explosion=Toolkit.getDefaultToolkit().getImage("explosion.jpg");
	}
	public void moverRoca()
	{
		if(x<-40)
		{
			this.initRoca();
		}
		else
		{
			x-=INTERVALO;
		}
	}
	public void dibujaRoca(Graphics g)
	{
		if(!exploto)
		{
			g.drawImage(roca,x,y,panel);
		}
		else
		{
			if(contador<10)
			{
				g.drawImage(explosion,x,y-30,panel);
				contador++;
			}
			else
			{
				this.initRoca();
				exploto=false;
				contador=0;
			}
		}
	}
	public void controlaChoke(int xNave, int yNave, boolean disparo)
	{
		if(!exploto)
		{
			if(xNave+90>=x && xNave<=x+85 && yNave+40>=y && yNave<=y+40)
			{
				panel.naveExplosion();
				exploto=true;
			}
			else if(disparo && yNave+34>=y && yNave+34<=y+40)
			{
				panel.sumaPuntos();	
				exploto=true;
			}
		}
	}
	public void initRoca()
	{
		x=1000;
		y=(int)Math.floor(Math.random()*450);
		exploto=false;
	}
}