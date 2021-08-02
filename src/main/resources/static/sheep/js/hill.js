
export class Hill {
    constructor(color, speed, total) {
        this.color = color;
        this.speed = speed;
        this.total = total;
    }

    resize(stageWidth, stageHeight) {
        this.stageWidth = stageWidth;
        this.stageHeight = stageHeight;

        // stageWidth 보다 넓게 잡아서 화면 밖에서부터 양이 걸어오는 것을 자연스럽게 할 수 있다.
        this.gap = this.stageWidth / (this.total - 2);
        this.points = [];

        for(let idx = 0; idx < this.total; idx++) {
            this.points[idx] = {
                x: idx * this.gap,
                y: this.getY()
            };
        }
    }

    draw(ctx) {
        ctx.fillStyle = this.color;
        ctx.beginPath();

        let cur = this.points[0];
        let prev = cur;
        let dots = [];
        cur.x += this.speed;
                
        if(cur.x > -this.gap) {
            this.points.unshift({
                x: -(this.gap * 2),
                y: this.getY()
            });
        } 
        // else if(cur.x > this.stageWidth + this.gap) {
        //     this.points.splice(-1);
        // }

        //마지막 원소를 제거 (불필요한)
        if (this.points[this.points.length -1].x > this.stageWidth + 2 * this.gap) {
            this.points.splice(-1);
        }

        ctx.moveTo(cur.x, cur.y);
        let prevCx = cur.x;
        let prevCy = cur.y;
        for(let idx = 1; idx < this.points.length; idx++) {
            cur = this.points[idx];
            cur.x += this.speed;
            const cx = (prev.x + cur.x) / 2;
            const cy = (prev.y + cur.y) / 2; 
            ctx.quadraticCurveTo(prev.x, prev.y, cx, cy);
            dots[idx] = {
                x1: prevCx,
                y1: prevCy,
                x2: prev.x,
                y2: prev.y,
                x3: cx,
                y3: cy
            };
 
            prev = cur;
            prevCx = cx;
            prevCy = cy;
        }
        ctx.lineTo(prev.x, prev.y);
        ctx.lineTo(this.stageWidth, this.stageHeight);
        ctx.lineTo(this.points[0].x, this.stageHeight);

        ctx.fill();
        
        return dots; 
    }

    getY() {
        const min = this.stageHeight / 8;
        const max = this.stageHeight - min;
        return min + Math.random() * max;
    }

}