export class Sheep {
    constructor(image, stageWidth) {
        this.image = image;

        this.totalFrame = 8;
        this.curFrame = 0;

        this.imageWidth = 360;
        this.imageHeight = 300;

        this.sheepWidth = 180;
        this.sheepHeight = 150;

        this.sheepWidthHalf = this.sheepWidth / 2;
        this.x = stageWidth + this.sheepWidth;
        this.y = 0;
        this.speed = Math.random() * 2 + 1;

        this.fps = 24;
        this.fpsTime = 1000 / this.fps;
    }

    draw(ctx, t, dots) {
        // requestAnimationFrame()의 타임스탬프를 이용해서 frame을 맞춘다
        if(!this.time) {
            this.time = t;
        }
        const now = t - this.time;
        if(now > this.fpsTime) {
            this.time = t;
            this.curFrame += 1;
            if(this.curFrame == this.totalFrame) {
                this.curFrame = 0;
            }
        }
        
        this.animate(ctx, dots);
    }

    animate(ctx, dots) {
        this.x -= this.speed;
        const closest = this.getY(this.x, dots);
        this.y = closest.y;
        
        ctx.save();
        ctx.translate(this.x, this.y);
        ctx.rotate(closest.rotation);

        ctx.fillStyle = '#fff';

        //canvas에서 이미지를 그릴 때 drawImage() 사용
        //this.imageWidth * this.curFrame을 한다면 curFrame 별로 에니매이션 효과를 낼 수 있다.
        ctx.drawImage(
            this.image,
            this.imageWidth * this.curFrame,
            0,
            this.imageWidth,
            this.imageHeight,
            -this.sheepWidthHalf,
            -this.sheepHeight + 20, 
            this.sheepWidth,
            this.sheepHeight
        );

        ctx.restore();
    }
    // 여러 곡선(dots) 중 해당 양(x 좌표)이 위에 있는 곡선(dot) 찾기 
    getY(x, dots) {
        for(let idx = 1; idx < dots.length; idx++) {
            if(x >= dots[idx].x1 && x <= dots[idx].x3) {
                return this.getY2(x, dots[idx]);
            }
        }

        return {
            y: 0,
            rotation: 0
        }
    }

    //곡선을 200으로 나눈 지점에서 현재 양의 y좌표를 찾기
    getY2(x, dot) {
        const total = 200;
        let pt = this.getPointOnQuad(dot.x1, dot.y1, dot.x2, dot.y2, dot.x3, dot.y3, 0);
        let prevX = pt.x;
        for(let idx = 1; idx < total; idx++) {
            const t = idx / total;
            pt = this.getPointOnQuad(dot.x1, dot.y1, dot.x2, dot.y2, dot.x3, dot.y3, t);
            
            if(x >= prevX && x <= pt.x) {
                return pt;
            }
            prevX = pt.x;
        }

        return pt;
    }
    // 선을 따라 이미지를 구하기 위해 값이 필요
    //// (s²)A + 2(st)B + (t²)C
    getQuadraticCurveValue(p0, p1, p2, t) {
        return ((1-t) * (1 - t) * p0) + (2 * (1 - t) * t * p1) + (t * t * p2);
    }

    getPointOnQuad(x1, y1, x2, y2, x3, y3, t) {
        const tx = this.getTangent(x1, x2, x3, t);
        const ty = this.getTangent(y1, y2, y3, t);

        // const rotation = -Math.atan2(tx, ty) +(90 * Math.PI / 180); // 90를 더해서 수직을 수평으로 변환 ,degree -> radian(단위 변화) : PI/ 180
        const rotation = Math.atan2(ty,tx);

        return {
            x : this.getQuadraticCurveValue(x1, x2, x3, t),
            y : this.getQuadraticCurveValue(y1, y2, y3, t),
            rotation: rotation
        }
    }

    // 양을 접선의 기울기 만큼 회전하기 위해서 Bezier Curve의 이차방정식 공식
    // f(t) = a+2(-a+b)t+(a-2b+c)t^2
    //2차 방적식을 미분하여 t에 따른 접선의 공식
    //f'(t) = 2(1-t)(b-a)+2(c-b)t
    getTangent(a, b, c, t) {
        return 2 * (1 - t) * (b - a) + 2 * (c - b) * t;
    }
}