/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package structures;

/**
 *
 * @author pavelgulaev
 */

/** Вообще сложная ситуация.
* При вычислении координат пептида в базе известно в константном он регионе или 
* нет, но неизвестно конкретных координат в предв. последовательности, поэтому 
* записываются относительные координаты в белке и isConstantRegion = true.
* При вычислении же предварительной последовательности, становятся известны
* конкретные координаты в предварительной последовательности, однако флаг 
* isConstantRegion становится бесполезным и даже вредным.
*/

public class PepCoordinates {
    public int left;
    public int right;
    public boolean isConstantRegion = false;

    public PepCoordinates(int left, int right) {
        this.left = left;
        this.right = right;
    }

    
    
    public PepCoordinates(int left, int right, boolean isConstantRegion) {
        this.left = left;
        this.right = right;
        this.isConstantRegion = isConstantRegion;
    }

    @Override
    public String toString() {
        return "PepCoordinates{" + "left=" + (left + 1) + ", right=" + (right + 1) + '}';
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 47 * hash + this.left;
        hash = 47 * hash + this.right;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final PepCoordinates other = (PepCoordinates) obj;
        if (this.left != other.left) {
            return false;
        }
        if (this.right != other.right) {
            return false;
        }
        return true;
    }
    
    
    
    
}