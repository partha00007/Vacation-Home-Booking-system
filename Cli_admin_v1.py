import click

@click.group()
def cli():
    """Cli tools fo admin"""
   
    pass

#Define a Command for Greeting Users

@click.command()
@click.argument("username")

def greet(username):
    """Greet a user"""
    click.echo(f"Hello and welcome, {username}!")


#Define a Command for system status  and verbose mode defines how much extra info you want the script to give

@click.command()
@click.option("--verbose", is_flag=True, help="Enable verbose mode")
def status(verbose):
    """show system status"""
    if verbose:
        click.echo("System Status: Running smoothly with detailed logs.")
    else:
        click.echo("System Status: Running.")
cli.add_command(greet)
cli.add_command(status)       
if __name__ == "__main__":
    cli()
