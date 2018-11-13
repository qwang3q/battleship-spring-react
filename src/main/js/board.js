const React = require('react');
const ReactDOM = require('react-dom');
const client = require('./client');

class App extends React.Component {
    constructor(props) {
		super(props);
        this.state = { 
            height : 10,
            width: 10,
            userCells: [],
            computerCells: [],
            userDefeated: "false",
            computerDefeated: "false"
        };

        this.loadFromServer = this.loadFromServer.bind(this);
        this.cellUpdate = this.cellUpdate.bind(this);
	}

    loadFromServer() {
        client({method: 'GET', path: '/board?type=userFleetBoard'}).done(response => {
            client({method: 'GET', path: '/boards/' + response.entity.id + '/cells'}).done(response2 => {
                this.setState({userCells: response2.entity._embedded.cells.sort((a,b) =>
                    (a.idOnBoard > b.idOnBoard ? 1 : -1)
                ) });
            });
        });

        client({method: 'GET', path: '/isdefeated?name=userFleetBoard'}).done(response => {
            this.setState({userDefeated: response.entity})
        });

        client({method: 'GET', path: '/board?type=computerFleetBoard'}).done(response => {
            client({method: 'GET', path: '/boards/' + response.entity.id + '/cells'}).done(response2 => {
                this.setState({computerCells: response2.entity._embedded.cells.sort((a,b) =>
                    (a.idOnBoard > b.idOnBoard ? 1 : -1)
                ) });
            });
        });

        client({method: 'GET', path: '/isdefeated?name=computerFleetBoard'}).done(response => {
            this.setState({computerDefeated: response.entity})
        });
    }

    getCell(cell) {
        client({method: 'GET', path: cell._links.self.href}).done(response => {
            return response.entity;
        });
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
        return (
            <div>
                <Board key="userFleetBoard" cellUpdate={this.cellUpdate}  cells={this.state.userCells} defeated={this.state.userDefeated} type="userFleetBoard" />
                <Board key="computerFleetBoard" cellUpdate={this.cellUpdate}  cells={this.state.computerCells} defeated={this.state.computerDefeated} type="computerFleetBoard" />
            </div>
        )
    }
}

class Board extends React.Component {

	constructor(props) {
		super(props);
        this.state = { 
            height : 10,
            width: 10
        };

        this.getCellDisplayStyle = this.getCellDisplayStyle.bind(this);
        this.getCellDisplayVal = this.getCellDisplayVal.bind(this);
	}

    getCellDisplayVal(cell) {
        if(cell.sunk == true) {
            return "O"
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

    getCellDisplayStyle(cell, boardType) {
        let cumtomCss = "invisible"
        if(boardType == "userFleetBoard") {
            cumtomCss = cell.hit == true ? "visible" : cell.shipCell == true ? "visible" : "invisible";
        } else {
            cumtomCss = cell.hit == false ? "invisible" : "visible";
        }
        return "square board-cell " + cumtomCss;
    }

	render() {
        const foobar = "   ";
        const map = this.props.cells.map ( cell =>
            <Cell key={cell._links.self.href} 
                cell={cell} 
                cellUpdate={this.props.cellUpdate} 
                type={this.props.type}
                cellVal={this.getCellDisplayVal(cell)}
                cellStyle={this.getCellDisplayStyle(cell, this.props.type)} />
        )
        
        let gameStatus = this.props.type == "userFleetBoard" ? "User Map" : "Computer Map";
        if(this.props.defeated == "true" || this.props.defeated == true) {
            gameStatus = this.props.type == "userFleetBoard" ? "You Lose" : "Computer Lose";
            let message = this.props.type == "userFleetBoard" ? "You Lose" : "Congratulations! You Win!"
            alert(message);
            client({method: 'GET', path: "/newgame"}).done(response => {

            });
        }

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
        this.props.cellUpdate(this.props.cell, this.props.type);
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
        alert("Starting new game");
        //window.location.reload();
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
	<App/>,
	document.getElementById('Boards')
)

// ReactDOM.render(
// 	<Board type="computerFleetBoard" />,
// 	document.getElementById('computerFleetBoard')
// )

ReactDOM.render(
	<NewGame />,
	document.getElementById('newGame')
)
