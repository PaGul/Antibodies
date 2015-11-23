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
    
    
}