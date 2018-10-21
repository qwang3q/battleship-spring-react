const React = require('react');
const ReactDOM = require('react-dom');
const client = require('./client');

class Board extends React.Component {

	constructor(props) {
		super(props);
        this.state = { 
            height : 10,
            width: 10,
            cells: [],
            rows: [],
            defeated: false
        };

        this.loadFromServer = this.loadFromServer.bind(this);
        this.cellUpdate = this.cellUpdate.bind(this);
        this.getCellDisplayStyle = this.getCellDisplayStyle.bind(this);
        this.getCellDisplayVal = this.getCellDisplayVal.bind(this);
	}

    loadFromServer() {
        client({method: 'GET', path: '/board?type=' + this.props.type}).done(response => {
            client({method: 'GET', path: '/boards/' + response.entity.id + '/cells'}).done(response2 => {
                this.setState({cells: response2.entity._embedded.cells.sort((a,b) =>
                    (a.idOnBoard > b.idOnBoard ? 1 : -1)
                ) });
            });
        });

        client({method: 'GET', path: '/isdefeated?href=' + this.props.type}).done(response => {
            this.setState({defeated: response})
        });
    }

    getCell(cell) {
        client({method: 'GET', path: cell._links.self.href}).done(response => {
            return response.entity;
        });
    }

    getCellDisplayVal(cell) {
        if(cell.sunk == true) {
            return "."
        }
        if(cell.shipCell == true) {
            if(cell.hit == true) {
                return "X"
            }
            return "S"
        }
        if(cell.hit == true) {
            return "~"
        }
        return "-"
    }

    getCellDisplayStyle(cell) {
        let cumtomCss = "invisible"
        if(this.props.type == "userFleetBoard") {
            cumtomCss = cell.hit == true ? "visible" : cell.shipCell == true ? "visible" : "invisible";
        } else {
            cumtomCss = cell.hit == false ? "invisible" : "visible";
        }
        return "square board-cell " + cumtomCss;
    }

    cellUpdate(cell) {
        let id = cell._links.self.href.split('/')[4];
        client({method: 'GET', path: "/hitcell?id=" + id}).done(response => {

        });

        this.loadFromServer();
    }

	componentDidMount() {
        this.loadFromServer();
	}

	render() {
        const foobar = "   ";
        const map = this.state.cells.map ( cell =>
            <Cell key={
                cell._links.self.href} 
                cell={cell} 
                cellUpdate={this.cellUpdate} 
                type="User"
                cellVal={this.getCellDisplayVal(cell)}
                cellStyle={this.getCellDisplayStyle(cell)} />
        )

        const gameStatus = this.state.defeated ? "YOU LOSE" : this.props.type

        return (
        <div className="game-board">
            <div className="status">{gameStatus}</div>
            {map}
            <div className="status">{foobar}</div>
        </div>
        )
    }
}



class Cell extends React.Component {
    constructor(props) {
        super(props);
        this.handleHit = this.handleHit.bind(this);
    }

    handleHit() {
        this.props.cellUpdate(this.props.cell);
    }

    render() {
        return (
            <button
                className={this.props.cellStyle}
                onClick={this.handleHit}
            >
              {this.props.cellVal}
            </button>
        )
    }
}


class NewGame extends React.Component {
    constructor(props) {
        super(props);
        this.handleClick = this.handleClick.bind(this);
    }

    handleClick() {
        client({method: 'GET', path: "/newgame"}).done(response => {

        });
    }

    render() {
        return (
            <button
                onClick={this.handleClick}
            >
              Start New Game
            </button>
        )
    }
}



ReactDOM.render(
	<Board type="userFleetBoard"/>,
	document.getElementById('userFleetBoard')
)

ReactDOM.render(
	<Board type="computerFleetBoard" />,
	document.getElementById('computerFleetBoard')
)

ReactDOM.render(
	<NewGame />,
	document.getElementById('newGame')
)
