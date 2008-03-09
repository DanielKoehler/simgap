package net.sf.gap.mc.qagesa.users.impl;

import eduni.simjava.distributions.*;
public class ZipF {
	
	int m_size;
	double m_theta;
	double[] m_prob;  
	
	Sim_random_obj m_rand;
	
	public ZipF(int size, double theta) 
	{
                m_rand = new Sim_random_obj("probability");
                m_size = size;
		m_theta = theta;
		m_prob = new double[size];
		
		int i;
		double sum = 0;
		for (i=0; i<size; i++) {
			m_prob[i]= Math.pow(1.0/(i+1), m_theta);
			sum += m_prob[i];
		}
		for (i = 0; i < size; i++) {
			m_prob[i] /= sum;
			if (i > 0)
				m_prob[i] += m_prob[i-1];	// just a trick for selecting an element
											// using a random number
		}		
	}
        
	public int getSize() {
		return m_size;
	}
	
	public double[] getProbs() {
		return m_prob;
	}
	public int probe() {
  		double r = m_rand.sample() * m_prob[m_size-1];

  		for (int i=0; i<m_size; i++)
    		if (r <= m_prob[i])
      			return i;

  		System.out.println("ZIPF: There is something wrong here.");
  		return m_size;
  	}
}
