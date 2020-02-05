//---------------------------------------------
//			CANVAS
//---------------------------------------------

import java.awt.*;
import java.awt.image.*;
import java.awt.event.*;
public class MiCanvas extends Canvas implements KeyListener,Runnable
{
	public Bola bola;
	public Barra barra;
	public Thread runner;
	public Graphics gBuffer;
	public Image imag;
	public int x=110;
	public final int dimBola=20;
	public final int anchoBarra=70;
	public final int altoBarra=20;
	public final int dimCanvas=500;
	public final int numLadrillos=54;
	public final int anchoLadrillo=50;
	public final int altoLadrillo=20;
	public int vidas=5;
	public String marcaVidas;
	public Ladrillo[] ladrillos;
	public Font fuenteG;
	public Font fuenteP;
	public Font fuenteM;
	public int estado=1;
	// 1 antes de Jugar
	// 2 Jugando
	// 3 Pause
	// 4 Game Over
	public MiCanvas()
	{
		super();
		setBackground(Color.black);
		this.addKeyListener(this);
		this.setSize(dimCanvas,dimCanvas);
		marcaVidas=String.valueOf(vidas);
		fuenteG=new Font("Arial", Font.BOLD, 36);
		fuenteP=new Font("Verdana", Font.PLAIN, 10);
		fuenteM=new Font("Arial", Font.PLAIN, 18);
		bola=new Bola(dimBola,this);
		barra=new Barra();
		ladrillos=new Ladrillo[numLadrillos];
		initLadrillos();
		runner = new Thread(this);
		runner.start();
	}
	public void run()
	{
		while(true)
		{
			bola.mover();
			bola.barraColision(barra);
			for(int i=0;i<numLadrillos;i++)
			{
				if(ladrillos[i].tocado!=true)
				{
					ladrillos[i].colision(bola);
				}
			}
			try 
			{ 
				Thread.sleep(10); 
			}
			catch (Exception e)
			{
				
			}
			repaint();
		}
	}
	public void initLadrillos()
	{
		int xL=0;
		int yL=30;
		for(int i=0;i<numLadrillos;i++)
		{
			ladrillos[i]=new Ladrillo(xL,yL,anchoLadrillo,altoLadrillo);
			if(xL+anchoLadrillo>dimCanvas)
			{
				yL+=altoLadrillo+1;
				xL=0;
			}
			else
			{
				xL+=anchoLadrillo+1;
			}
		}
	}
	public void update(Graphics g) 
	{
		paint(g);
    }	
	public void paint(Graphics g)
	{
		if(gBuffer==null)
		{
       		imag=createImage(dimCanvas,dimCanvas);
        	gBuffer=imag.getGraphics();
		}
		actualizaCanvas(gBuffer);
		g.drawImage(imag,0,0,this);
	}
	public void actualizaCanvas(Graphics g)
	{
		g.setColor(Color.black);
		g.fillRect(0,0,dimCanvas,dimCanvas);
		if(estado==1)
		{
			g.setColor(Color.green);
			g.setFont(fuenteG);
			g.drawString("ARKANOID",160,180);
			g.setFont(fuenteP);
			g.drawString("Pulsa ENTER para comenzar",174,225);
			g.drawString("Edited by Juanjo",200,400);
		}
		else if(estado==2)
		{
			g.setColor(Color.white);
			g.fillRect(0,19,dimCanvas,10);
			g.setFont(fuenteP);
			g.drawString("VIDAS : "+marcaVidas,5,13);
			barra.draw(g);
			bola.draw(g);
			for(int i=0;i<numLadrillos;i++)
			{
				ladrillos[i].draw(g);
			}
		}
		else if(estado==3)
		{
			g.setColor(Color.green);
			g.setFont(fuenteM);
			g.drawString("Juego en pausa",184,210);
			g.drawString("Pulsa SPACE para continuar",135,245);
		}
		else if(estado==4)
		{
			g.setColor(Color.green);
			g.setFont(fuenteM);
			g.drawString("Has perdido tus vidas",184,210);
			g.drawString("Pulsa ENTER para comenzar de nuevo",110,245);
		}
	}
	public void restaVida()
	{
		this.vidas-=1;
		if(this.vidas==0)
		{
			this.estado=4;
		}
		marcaVidas=String.valueOf(vidas);
	}
	public void keyPressed(KeyEvent e)
	{
		int code=e.getKeyCode();
		if(code==e.VK_SPACE)
		{
			if(bola.stop==false)
			{
				bola.stop=true;
				this.estado=3;
			}
			else
			{
				bola.stop=false;
				this.estado=2;
			}
		}
		if(code==e.VK_ENTER)
		{
			if(this.estado==1)
			{
				this.estado=2;
			}
			else if (this.estado==4)
			{
				this.estado=1;
				this.vidas=5;
			}
		}
		if(code==e.VK_RIGHT)
		{
			if(x<dimCanvas-anchoBarra && bola.stop==false)
			{
				x+=40;
				barra.mover(x);
			}
		}
		else if(code==e.VK_LEFT && bola.stop==false)
		{
			if(x>0)
			{
				x-=40;
				barra.mover(x);
			}
		}
	}
	public void keyReleased(KeyEvent e){}
	public void keyTyped(KeyEvent e){}
	public static void main(String []args)
	{
		Frame vent=new Frame("Arkanoid");
		vent.add(new MiCanvas());
		vent.setSize(500,500);
		vent.show();
		vent.addWindowListener(new WindowAdapter(){public void windowClosing(WindowEvent ev){System.exit(0);}});
		vent.setResizable(false);
	}
}

//---------------------------------------------
//			BOLA
//---------------------------------------------

class Bola
{
	int x;
	int y;
	int intervaloX=2;
	int intervaloY=2;
	int dimensiones;
	boolean stop=true;
	final int dimCanvas=500;
	MiCanvas c;
	public Bola(int dim, MiCanvas canvas)
	{
		dimensiones=dim;
		x=250;
		y=150;
		c=canvas;
	}
	public void draw(Graphics g)
	{
		g.setColor(Color.WHITE);
		g.fillOval(x,y,20,20);
	}
	public void barraColision(Barra barra)
	{
		int izquierdaBarra=barra.x;
		int derechaBarra=barra.x+70;
		if(y==390 && x>izquierdaBarra && x<derechaBarra || y==390 && x+dimensiones>izquierdaBarra && x+dimensiones<derechaBarra)
		{
			intervaloY*=-1;
		}
	}
	public void mover()
	{
		if(!stop)
		{
			if(x<=dimCanvas-dimensiones)
			{
				x+=intervaloX;
				if(x==dimCanvas-dimensiones)
				{
					intervaloX*=-1;
				}
				else if(x==0)
				{
					intervaloX*=-1;
				}
			}
			if(y<=dimCanvas-dimensiones)
			{
				y+=intervaloY;
				if(y==dimCanvas-dimensiones)
				{
					c.restaVida();
					this.x=250;
					this.y=150;
					this.stop=true;
				}
				else if(y==30)
				{
					intervaloY*=-1;
				}
			}
		}
	}
}

//---------------------------------------------
//			BARRA
//---------------------------------------------

class Barra
{
	int x=110;
	int y=400;
	public Barra()
	{
	
	}
	public void draw(Graphics g)
	{
		g.setColor(Color.blue);
		g.fillRoundRect(x,y,70,20,15,15);
	}
	public void mover(int xBarra)
	{
		x=xBarra;
	}
}

//---------------------------------------------
//			LADRILLO
//---------------------------------------------

class Ladrillo
{
	int posX;
	int posY;
	int anchoLadrillo;
	int altoLadrillo;
	Color colorLadrillo;
	boolean tocado;
	final int dimBola=20;
	public Ladrillo(int x, int y,int ancho,int alto)
	{
		posX=x;
		posY=y;
		anchoLadrillo=ancho;
		altoLadrillo=alto;
		tocado=false;
		int color1=(int)Math.floor(Math.random()*200);
		int color2=(int)Math.floor(Math.random()*200);
		int color3=(int)Math.floor(Math.random()*200);
		colorLadrillo=new Color(color1,color2,color3);
	}
	public void colision(Bola bola)
	{
		int derechaBola=bola.x+dimBola;
		int izquierdaBola=bola.x;
		int derechaLadrillo=posX+anchoLadrillo;
		int altoBola=bola.y;
		int bajoBola=bola.y+dimBola;
		int bajoLadrillo=posY+altoLadrillo;
		final int intervaloColision=10;
		if(izquierdaBola>=posX && izquierdaBola<=derechaLadrillo || derechaBola>=posX && derechaBola<=derechaLadrillo)
		{
			if(altoBola<=bajoLadrillo)
			{
				if(bola.intervaloY<0)
				{
					bola.intervaloY*=-1;
					tocado=true;
				}
			}
		}
		if(altoBola>=posY && altoBola<=bajoLadrillo || bajoBola>=posY && bajoBola<=bajoLadrillo)
		{
			if(derechaBola>=posX && izquierdaBola-posX>=-bola.dimensiones && izquierdaBola-posX<=-intervaloColision)
			{
				bola.intervaloX*=-1;
				tocado=true;
			}
			if(izquierdaBola<=derechaLadrillo && derechaBola-derechaLadrillo<=bola.dimensiones && derechaBola-derechaLadrillo>=intervaloColision)
			{
				bola.intervaloX*=-1;
				tocado=true;
			}
		}
	}
	public void draw(Graphics g)
	{
		if(!tocado)
		{
			g.setColor(colorLadrillo);
			g.fillRect(posX, posY,anchoLadrillo,altoLadrillo);
		}
	}
}