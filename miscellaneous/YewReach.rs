use yew::prelude::*;

#[function_component(Counter)]
fn counter() -> Html {
    
    let count = useState(|| 0);
    let step = useState(|| 1);
    
    let increment = {
        
        let count = count.clone();
        let step = step.clone();
        
        Callback::from(move || {count.set(*count + *step)});
    };
    
    let decrement = {
        
        let count = count.clone();
        let step = step.clone();
        
        Callback::from(move ||  {count.set(*count - *step)});
        
    };
    
    let reset = {
        
        Callback::from(move || {count.set(0)})
    };
    
    let increaseStep = {
        
        let step = step.clone();
        Callback::from(move || step.set(*step + 1))
    };
    
    let decreaseStep = {
        
        let step = step.clone();
        
        Callback::from(move || step.set(*step - 1))
    };
    
    html! {
        <div style = "font-family: padding:20px; margin: auto">
        <h1>{"Counter"}</h1>
        <div style="margin-bottom:20px;">
        <p>{ format!("Current count: {}", *count) }</p>
        <p>{ format!("Step size: {}", *step) }</p>
        </div>

        <div style="display:flex; gap:10px; margin-bottom:15px;">
                <button onclick={increment}>{"Increment"}</button>
                <button onclick={decrement}>{"Decrement"}</button>
                <button onclick={reset}>{ "Reset" }</button>
        </div>

        <div style="display:flex; gap:10px;">
                <button onclick={increase_step}>{ "Increase Step" }</button>
                <button onclick={decrease_step}>{ "Decrease Step" }</button>
        </div>
        </div>
    }
}

fn main() 
{
    yew::Renderer::<Counter>::new().rend();
}